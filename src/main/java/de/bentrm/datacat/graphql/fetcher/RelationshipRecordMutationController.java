package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordServiceFactory;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.graphql.input.CreateRelationshipInput;
import de.bentrm.datacat.graphql.input.DeleteRelationshipInput;
import de.bentrm.datacat.graphql.input.RelationshipPropertiesInput;
import de.bentrm.datacat.graphql.payload.CreateRelationshipPayload;
import de.bentrm.datacat.graphql.payload.DeleteRelationshipPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
public class RelationshipRecordMutationController {

    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CatalogCleanupService catalogCleanupService;

    @Autowired
    private SimpleRecordServiceFactory simpleRecordServiceFactory;

    @Autowired
    private RelationshipToPropertyRecordService relationshipToPropertyRecordService;

    @Autowired
    private RelationshipToSubjectRecordService relationshipToSubjectRecordService;

    public CatalogRecord createObjectRelationship(CreateRelationshipInput input) {
        final RelationshipPropertiesInput inputProperties = input.getProperties();
        Optional<CatalogRecord> s = Optional.empty();
        String relId = null;
        if (inputProperties.getId() != null && !inputProperties.getId().isEmpty()) {
            relId = inputProperties.getId();
            s = catalogService.getEntryById(relId);
        } else {
            relId = UUID.randomUUID().toString();
        }
        

        if (input.getRelationshipType().getRelationProperty().equals("RelationshipToSubject")) {
            XtdRelationshipToSubject catalogEntry;
            if (s.isPresent()) {
                catalogEntry = (XtdRelationshipToSubject) s.get();
                catalogEntry = relationshipToSubjectRecordService.setRelatedRecords(catalogEntry,
                        input.getToIds());
            } else {
                catalogEntry = relationshipToSubjectRecordService.addRecord(relId, input.getFromId(),
                        input.getToIds());
                
                // Prüfe ob ein Name vorhanden ist
                String name = inputProperties.getRelationshipToSubjectProperties().getName();
                if (name != null && !name.isEmpty()) {
                    // Verwende die neue Methode die nach bestehendem Type sucht oder neuen erstellt
                    catalogEntry = relationshipToSubjectRecordService.addRelationshipTypeByName(catalogEntry,
                            name,
                            inputProperties.getRelationshipToSubjectProperties().getRelationshipType());
                } else {
                    // Bisheriges Verhalten: Erstelle immer neues XtdRelationshipType
                    catalogEntry = relationshipToSubjectRecordService.addRelationshipType(catalogEntry,
                            inputProperties.getRelationshipToSubjectProperties().getRelationshipType());
                }
            }
            return catalogEntry;
        } else {
            XtdRelationshipToProperty catalogEntry;
            if (s.isPresent()) {
                catalogEntry = (XtdRelationshipToProperty) s.get();
                catalogEntry = relationshipToPropertyRecordService.setRelatedRecords(catalogEntry,
                        input.getToIds());
                
            } else {
                catalogEntry = relationshipToPropertyRecordService
                    .addRecord(relId, input.getFromId(), input.getToIds());
            catalogEntry = relationshipToPropertyRecordService.addRelationshipType(catalogEntry,
                    inputProperties.getRelationshipToPropertyProperties().getRelationshipType());
            }
            return catalogEntry;
        }
    }

    @MutationMapping
    protected CreateRelationshipPayload createRelationship(@Argument CreateRelationshipInput input) {
        final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId())
                .orElseThrow(() -> new NoSuchElementException("Catalog record for fromId not found"));
        final SimpleRecordService<?> simpleRecordService = simpleRecordServiceFactory
                .getService(CatalogRecordType.getByDomainClass(fromEntity));

        CatalogRecord record;
        SimpleRelationType relType = input.getRelationshipType();
        if (relType == SimpleRelationType.RelationshipToSubject
                || relType == SimpleRelationType.RelationshipToProperty) {
            record = createObjectRelationship(input);
        } else if (relType == SimpleRelationType.Values) {
            if (simpleRecordService instanceof ValueListRecordService) {
                Integer order = (input.getProperties() != null && input.getProperties().getValueListProperties() != null)
        ? input.getProperties().getValueListProperties().getOrder()
        : null;
            record = ((ValueListRecordService) simpleRecordService).setOrderedValues(input.getFromId(), input.getToIds(), relType, order);
            } else {
                throw new IllegalArgumentException("Invalid service for ordered values: " + simpleRecordService.getClass().getName());
            }
        } else {
            record = simpleRecordService.setRelatedRecords(input.getFromId(), input.getToIds(), relType);
        }

        return PAYLOAD_MAPPER.toCreateRelationshipPayload(record);
    }

    protected DeleteRelationshipPayload deleteObjectRelationship(String relationshipId) {
        CatalogRecord relationship = catalogService.getEntryById(relationshipId)
                .orElseThrow(() -> new NoSuchElementException("Catalog record for relationshipId not found"));

        if (relationship instanceof XtdRelationshipToSubject) {
            String relationshipTypeId = ((XtdRelationshipToSubject) relationship).getRelationshipType().getId();
            relationship = relationshipToSubjectRecordService.removeRecord(relationshipId);
            
            // Lösche den RelationshipType nur, wenn er nicht mehr von anderen Relationen verwendet wird
            Long usageCount = relationshipToSubjectRecordService.countRelationshipsUsingRelationshipType(relationshipTypeId);
            if (usageCount == 0) {
                catalogCleanupService.deleteNodeWithRelationships(relationshipTypeId);
            }
        } else {
            relationship = relationshipToPropertyRecordService.removeRecord(relationshipId);
        }
        return PAYLOAD_MAPPER.toDeleteRelationshipPayload(relationship);
    }

    @MutationMapping
    protected DeleteRelationshipPayload deleteRelationship(@Argument DeleteRelationshipInput input) {
        log.info("Deleting relationship: type={}, from={}, to={}, name={}", 
            input.getRelationshipType(), input.getFromId(), input.getToId(), input.getName());
        
        SimpleRelationType relType = input.getRelationshipType();
        CatalogRecord record;
        final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId())
                .orElseThrow(() -> new NoSuchElementException("Catalog record for fromId not found"));
        final SimpleRecordService<?> simpleRecordService = simpleRecordServiceFactory
                .getService(CatalogRecordType.getByDomainClass(fromEntity));
        if (relType == SimpleRelationType.RelationshipToSubject
                || relType == SimpleRelationType.RelationshipToProperty) {
            String relationshipId = catalogService.getRelationshipBetweenObjectsByName(
                input.getFromId(), 
                input.getToId(), 
                input.getName()
            );
            SimpleRelationType target;
            if (relType == SimpleRelationType.RelationshipToSubject) {
                target = SimpleRelationType.TargetSubjects;
            } else {
                target = SimpleRelationType.TargetProperties;
            }
            record = simpleRecordService.removeRelationship(relationshipId, input.getToId(), target);
            Long count = catalogService.countTargetRelationships(relationshipId);
            if (count == 0) {
                deleteObjectRelationship(relationshipId);
            }
        } else {
            log.info("Removing simple relationship: relationType={}", relType.getRelationProperty());
            record = simpleRecordService.removeRelationship(input.getFromId(), input.getToId(), relType);
        }
        log.info("Relationship deleted successfully for record: {}", record.getId());
        return PAYLOAD_MAPPER.toDeleteRelationshipPayload(record);
    }
}
