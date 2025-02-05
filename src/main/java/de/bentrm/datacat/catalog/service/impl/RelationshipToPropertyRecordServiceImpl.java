package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.Enums.XtdPropertyRelationshipTypeEnum;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.repository.RelationshipToPropertyRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import de.bentrm.datacat.catalog.service.dto.RelationshipToPropertyDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.ConnectingPropertyDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.TargetPropertiesDtoProjection;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class RelationshipToPropertyRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdRelationshipToProperty, RelationshipToPropertyRepository>
        implements RelationshipToPropertyRecordService {

@Autowired
@Lazy
private PropertyRecordService propertyRecordService;

@Autowired
private ObjectRecordService objectRecordService;

    public RelationshipToPropertyRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     RelationshipToPropertyRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdRelationshipToProperty.class, neo4jTemplate, repository, cleanupService);
    }

    @Transactional
    @Override
    public XtdRelationshipToProperty addRecord(@NotBlank String id,
            @NotBlank String relatingRecordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds) {
        try {
            XtdRelationshipToProperty newRecord = this.getDomainClass().getDeclaredConstructor().newInstance();

            newRecord.setId(id);
            log.trace("Persisting new relationship record...");
            newRecord = this.getRepository().save(newRecord);

            log.trace("Setting relating record with id: {}", relatingRecordId);
            newRecord = this.setRelatingRecord(newRecord, relatingRecordId);
            log.info("Set relating record with id: {}", relatingRecordId);

            log.trace("Setting related records with ids: {}", relatedRecordIds);
            newRecord = this.setRelatedRecords(newRecord, relatedRecordIds);
            log.info("Set related records with ids: {}", relatedRecordIds);

            log.trace("Persisted new relationship record with id: {}", newRecord.getId());

            return newRecord;
        } catch (ReflectiveOperationException e) {
            log.warn("Can not instantiate catalog records of type: {}", this.getDomainClass());
            throw new IllegalArgumentException("unsupported record type");
        }
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipToProperty;
    }

    @Override
    public @NotNull XtdProperty getConnectingProperty(@NotNull XtdRelationshipToProperty relationshipToProperty) {
        Assert.notNull(relationshipToProperty, "RelationshipToProperty must not be null");
        final String propertyId = getRepository().findConnectingPropertyId(relationshipToProperty.getId());
        return propertyRecordService.findByIdWithDirectRelations(propertyId).orElseThrow(() -> new IllegalArgumentException("No record with id " + propertyId + " found."));
    }

    @Override
    public @NotNull List<XtdProperty> getTargetProperties(@NotNull XtdRelationshipToProperty relationshipToProperty) {
        Assert.notNull(relationshipToProperty, "RelationshipToProperty must not be null");
        final List<String> propertyIds = getRepository().findTargetPropertyIds(relationshipToProperty.getId());
        final Iterable<XtdProperty> properties = propertyRecordService.findAllEntitiesById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    protected XtdRelationshipToProperty setRelatingRecord(@NotNull XtdRelationshipToProperty relationshipRecord,
                                     @NotBlank String relatingRecordId) {
        final XtdProperty relatingCatalogRecord = propertyRecordService
                .findByIdWithDirectRelations(relatingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + relatingRecordId + " found."));
        relationshipRecord.setConnectingProperty(relatingCatalogRecord);
        neo4jTemplate.saveAs(relationshipRecord, ConnectingPropertyDtoProjection.class);
        return relationshipRecord;
    }

    @Transactional
    @Override
    public @NotNull XtdRelationshipToProperty setRelatedRecords(@NotNull XtdRelationshipToProperty relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdProperty> items = propertyRecordService.findAllEntitiesById(relatedRecordIds);
        final List<XtdProperty> related = StreamSupport
                .stream(items.spliterator(), false)
                .collect(Collectors.toList());

        relationshipRecord.getTargetProperties().addAll(related);
        neo4jTemplate.saveAs(relationshipRecord, TargetPropertiesDtoProjection.class);
        return relationshipRecord;
    }

    @Transactional
    @Override
    public @NotNull XtdRelationshipToProperty setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        XtdRelationshipToProperty relationship = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {

            case TargetProperties -> {
                relationship = setRelatedRecords(relationship, relatedRecordIds);
            }
            default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", relationship);
        return relationship;
    }  


    @Transactional
    @Override
    public @NotNull XtdRelationshipToProperty addRelationshipType(@NotNull XtdRelationshipToProperty relationship, @NotNull XtdPropertyRelationshipTypeEnum relationshipType) {
        relationship.setRelationshipType(relationshipType);
        neo4jTemplate.saveAs(relationship, RelationshipToPropertyDtoProjection.class);
        return relationship;
    }
}
