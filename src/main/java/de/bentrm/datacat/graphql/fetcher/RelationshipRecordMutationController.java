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
        Optional<CatalogRecord> s = catalogService.getEntryById(inputProperties.getId());

        if (input.getRelationshipType().getRelationProperty().equals("RelationshipToSubject")) {
            XtdRelationshipToSubject catalogEntry;
            if (s.isPresent()) {
                catalogEntry = (XtdRelationshipToSubject) s.get();
                catalogEntry = relationshipToSubjectRecordService.setRelatedRecords(catalogEntry,
                        input.getToIds());
            } else {
                catalogEntry = relationshipToSubjectRecordService.addRecord(inputProperties.getId(), input.getFromId(),
                        input.getToIds());
                catalogEntry = relationshipToSubjectRecordService.addRelationshipType(catalogEntry,
                        inputProperties.getRelationshipToSubjectProperties().getRelationshipType());
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
                    .addRecord(inputProperties.getId(), input.getFromId(), input.getToIds());
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
            catalogCleanupService.deleteNodeWithRelationships(relationshipTypeId);
            relationship = relationshipToSubjectRecordService.removeRecord(relationshipId);
        } else {
            relationship = relationshipToPropertyRecordService.removeRecord(relationshipId);
        }
        return PAYLOAD_MAPPER.toDeleteRelationshipPayload(relationship);
    }

    @MutationMapping
    protected DeleteRelationshipPayload deleteRelationship(@Argument DeleteRelationshipInput input) {
        SimpleRelationType relType = input.getRelationshipType();
        CatalogRecord record;
        final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId())
                .orElseThrow(() -> new NoSuchElementException("Catalog record for fromId not found"));
        final SimpleRecordService<?> simpleRecordService = simpleRecordServiceFactory
                .getService(CatalogRecordType.getByDomainClass(fromEntity));
        if (relType == SimpleRelationType.RelationshipToSubject
                || relType == SimpleRelationType.RelationshipToProperty) {
            String relationshipId = catalogService.getRelationshipBetweenObjects(input.getFromId(), input.getToId());
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
            record = simpleRecordService.removeRelationship(input.getFromId(), input.getToId(), relType);
        }
        return PAYLOAD_MAPPER.toDeleteRelationshipPayload(record);
    }
}
