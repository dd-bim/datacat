package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.repository.RelationshipToPropertyRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
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
        extends AbstractRelationshipRecordServiceImpl<XtdRelationshipToProperty, RelationshipToPropertyRepository>
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

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipToProperty;
    }

    @Override
    public @NotNull XtdProperty getConnectingProperty(@NotNull XtdRelationshipToProperty relationshipToProperty) {
        Assert.notNull(relationshipToProperty, "RelationshipToProperty must not be null");
        final String propertyId = getRepository().findConnectingPropertyId(relationshipToProperty.getId());
        return propertyRecordService.findByIdWithDirectRelations(propertyId).orElseThrow();
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
   @Override
   public @NotNull XtdRelationshipToProperty setRelatedRecords(@NotBlank String relationshipId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds) {

       final XtdRelationshipToProperty relationship = getRepository().findByIdWithDirectRelations(relationshipId).orElseThrow();

       final Iterable<XtdProperty> items = propertyRecordService.findAllEntitiesById(relatedRecordIds);
       final List<XtdProperty> related = StreamSupport
               .stream(items.spliterator(), false)
               .collect(Collectors.toList());

       relationship.getTargetProperties().clear();
       relationship.getTargetProperties().addAll(related);

       final XtdRelationshipToProperty persistentRelationship = getRepository().save(relationship);
       log.trace("Updated relationship: {}", persistentRelationship);
       return persistentRelationship;
   }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelationshipToProperty relationshipRecord,
                                     @NotBlank String relatingRecordId) {
        final XtdProperty relatingCatalogRecord = propertyRecordService
                .findByIdWithDirectRelations(relatingRecordId)
                .orElseThrow();
        relationshipRecord.setConnectingProperty(relatingCatalogRecord);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelationshipToProperty relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdProperty> items = propertyRecordService.findAllEntitiesById(relatedRecordIds);
        final List<XtdProperty> related = StreamSupport
                .stream(items.spliterator(), false)
                .collect(Collectors.toList());
        relationshipRecord.getTargetProperties().clear();
        relationshipRecord.getTargetProperties().addAll(related);
    }

    @Transactional
    @Override
    public @NotNull XtdRelationshipToProperty setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdRelationshipToProperty relationship = getRepository().findByIdWithDirectRelations(recordId).orElseThrow();

        switch (relationType) {

            case TargetProperties -> {
                final Iterable<XtdProperty> targetProperties = propertyRecordService.findAllEntitiesById(relatedRecordIds);
                final List<XtdProperty> relatedTargetProperties = StreamSupport
                        .stream(targetProperties.spliterator(), false)
                        .collect(Collectors.toList());

                relationship.getTargetProperties().clear();
                relationship.getTargetProperties().addAll(relatedTargetProperties);
            }
            default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        final XtdRelationshipToProperty persistentRelationship = getRepository().save(relationship);
        log.trace("Updated relationship: {}", persistentRelationship);
        return persistentRelationship;
    }  
}
