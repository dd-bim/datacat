package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.repository.RelationshipToPropertyRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final PropertyRepository propertyRepository;
    private final ObjectRecordService objectRecordService;

    public RelationshipToPropertyRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     RelationshipToPropertyRepository repository,
                                     PropertyRepository propertyRepository,
                                     ObjectRecordService objectRecordService,
                                     CatalogCleanupService cleanupService) {
        super(XtdRelationshipToProperty.class, neo4jTemplate, repository, cleanupService);
        this.propertyRepository = propertyRepository;
        this.objectRecordService = objectRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipToProperty;
    }

   @Transactional
   @Override
   public @NotNull XtdRelationshipToProperty setRelatedRecords(@NotBlank String relationshipId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds) {

       final XtdRelationshipToProperty relationship = getRepository().findById(relationshipId).orElseThrow();

       final Iterable<XtdProperty> items = propertyRepository.findAllById(relatedRecordIds);
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
        final XtdProperty relatingCatalogRecord = propertyRepository
                .findById(relatingRecordId)
                .orElseThrow();
        relationshipRecord.setConnectingProperty(relatingCatalogRecord);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelationshipToProperty relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdProperty> items = propertyRepository.findAllById(relatedRecordIds);
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

        final XtdRelationshipToProperty relationship = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {

            case TargetProperties -> {
                final Iterable<XtdProperty> targetProperties = propertyRepository.findAllById(relatedRecordIds);
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
