package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.RelationshipRecordService;
import de.bentrm.datacat.catalog.service.RelationshipRecordServiceFactory;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.SimpleRecordServiceFactory;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.graphql.input.CreateRelationshipInput;
import de.bentrm.datacat.graphql.input.DeleteRelationshipInput;
import de.bentrm.datacat.graphql.input.RelationshipPropertiesInput;
import de.bentrm.datacat.graphql.input.DeleteObjectRelationshipInput;
import de.bentrm.datacat.graphql.payload.DeleteObjectRelationshipPayload;
import de.bentrm.datacat.graphql.payload.CreateRelationshipPayload;
import de.bentrm.datacat.graphql.payload.DeleteRelationshipPayload;
import de.bentrm.datacat.graphql.payload.PayloadMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Slf4j
@Controller
public class RelationshipRecordMutationController {

    private final PayloadMapper PAYLOAD_MAPPER = PayloadMapper.INSTANCE;

    @Autowired
    private  CatalogService catalogService;

    @Autowired
    private CatalogCleanupService catalogCleanupService;

    @Autowired
    private SimpleRecordServiceFactory simpleRecordServiceFactory;

    @Autowired
    private RelationshipRecordServiceFactory relationshipRecordServiceFactory;

    @Autowired
    private RelationshipToPropertyRecordService relationshipToPropertyRecordService;

    @Autowired
    private RelationshipToSubjectRecordService relationshipToSubjectRecordService;


    public CatalogRecord createObjectRelationship(CreateRelationshipInput input) {
            final RelationshipPropertiesInput inputProperties = input.getProperties();
            RelationshipRecordService<?> relationshipRecordService;
            if (input.getRelationshipType().getRelationProperty().equals("RelationshipToSubject")) {
                relationshipRecordService = relationshipRecordServiceFactory.getService(CatalogRecordType.RelationshipToSubject);
            } else {
                relationshipRecordService = relationshipRecordServiceFactory.getService(CatalogRecordType.RelationshipToProperty);
            }

            final AbstractRelationship catalogEntry = relationshipRecordService.addRecord(inputProperties.getId(), input.getFromId(),
                    input.getToIds());

            if (catalogEntry instanceof XtdRelationshipToProperty) {
                XtdRelationshipToProperty relationship = (XtdRelationshipToProperty) catalogEntry;
                relationship = relationshipToPropertyRecordService.addRelationshipType(relationship, inputProperties.getRelationshipToPropertyProperties().getRelationshipType());
                return relationship;
            } else {
                XtdRelationshipToSubject relationship = (XtdRelationshipToSubject) catalogEntry;
                relationship = relationshipToSubjectRecordService.addRelationshipType(relationship, inputProperties.getRelationshipToSubjectProperties().getRelationshipType());
                return catalogEntry;
            }
    }

    @MutationMapping
    protected DeleteObjectRelationshipPayload deleteObjectRelationship(@Argument DeleteObjectRelationshipInput input) {
        final String relationshipId = input.getRelationshipId();
        final AbstractRelationship relationship = catalogService.getRelationship(relationshipId).orElseThrow(() -> new NoSuchElementException("Catalog record for relationshipId not found"));
        final RelationshipRecordService<?> relationshipRecordService = relationshipRecordServiceFactory.getService(CatalogRecordType.getByDomainClass(relationship));

        if (relationship instanceof XtdRelationshipToSubject) {
            String relationshipTypeId = ((XtdRelationshipToSubject)relationship).getRelationshipType().getId();
            catalogCleanupService.deleteNodeWithRelationships(relationshipTypeId);
        }
        final AbstractRelationship deletedRelationship = relationshipRecordService.removeRecord(relationshipId);
        return PAYLOAD_MAPPER.toDeleteObjectRelationshipPayload(deletedRelationship);
    }

    @MutationMapping
    protected CreateRelationshipPayload createRelationship(@Argument CreateRelationshipInput input) {
        final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId()).orElseThrow(() -> new NoSuchElementException("Catalog record for fromId not found"));
        final SimpleRecordService<?> simpleRecordService = simpleRecordServiceFactory.getService(CatalogRecordType.getByDomainClass(fromEntity));

        CatalogRecord record;
        SimpleRelationType relType = input.getRelationshipType();
        if (relType == SimpleRelationType.RelationshipToSubject
                || relType == SimpleRelationType.RelationshipToProperty) {
            record = createObjectRelationship(input);
        } else if (simpleRecordService != null) {
            record = simpleRecordService.setRelatedRecords(input.getFromId(), input.getToIds(),
                    relType);
        } else {
            final RelationshipRecordService<?> relationshipRecordService = relationshipRecordServiceFactory.getService(CatalogRecordType.getByDomainClass(fromEntity));
            record = relationshipRecordService.setRelatedRecords(input.getFromId(), input.getToIds(),
                    relType);
        }

        return PAYLOAD_MAPPER.toCreateRelationshipPayload(record);
    }

    @MutationMapping
    protected DeleteRelationshipPayload deleteRelationship(@Argument DeleteRelationshipInput input) {
        SimpleRelationType relType = input.getRelationshipType();
        if (relType == SimpleRelationType.RelationshipToSubject
                || relType == SimpleRelationType.RelationshipToProperty) {
                    throw new IllegalArgumentException("The object relationships RelationshipToSubject and RelationshipToProperty have to be deleted with deleteObjectRelationship.");
                }
        final CatalogRecord fromEntity = catalogService.getEntryById(input.getFromId()).orElseThrow(() -> new NoSuchElementException("Catalog record for fromId not found"));
        final SimpleRecordService<?> simpleRecordService = simpleRecordServiceFactory.getService(CatalogRecordType.getByDomainClass(fromEntity));

        final CatalogRecord record = simpleRecordService.removeRelationship(input.getFromId(), input.getToId(), input.getRelationshipType());
        return PAYLOAD_MAPPER.toDeleteRelationshipPayload(record);
    }
}
