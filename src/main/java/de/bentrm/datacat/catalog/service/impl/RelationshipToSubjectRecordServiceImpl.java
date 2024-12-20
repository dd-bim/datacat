package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.RelationshipToSubjectRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.service.RelationshipTypeRecordService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class RelationshipToSubjectRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelationshipToSubject, RelationshipToSubjectRepository>
        implements RelationshipToSubjectRecordService {

            @Autowired
            @Lazy
            private SubjectRecordService subjectRecordService;

            @Autowired
            private ObjectRecordService objectRecordService;

            @Autowired
            private RelationshipTypeRecordService relationshipTypeRecordService;

    public RelationshipToSubjectRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     RelationshipToSubjectRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdRelationshipToSubject.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipToSubject;
    }

    @Override
    public @NotNull List<XtdSubject> getScopeSubjects(XtdRelationshipToSubject relationshipToSubject) {
        Assert.notNull(relationshipToSubject.getId(), "RelationshipToSubject must be persistent.");
        final List<String> subjectIds = getRepository().findAllScopeSubjectIdsAssignedToRelationshipToSubject(relationshipToSubject.getId());
        final Iterable<XtdSubject> subjects = subjectRecordService.findAllEntitiesById(subjectIds);

        return StreamSupport
                .stream(subjects.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<XtdSubject> getTargetSubjects(XtdRelationshipToSubject relationshipToSubject) {
        Assert.notNull(relationshipToSubject.getId(), "RelationshipToSubject must be persistent.");
        final List<String> subjectIds = getRepository().findAllTargetSubjectIdsAssignedToRelationshipToSubject(relationshipToSubject.getId());
        final Iterable<XtdSubject> subjects = subjectRecordService.findAllEntitiesById(subjectIds);

        return StreamSupport
                .stream(subjects.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull XtdSubject getConnectingSubject(XtdRelationshipToSubject relationshipToSubject) {
        Assert.notNull(relationshipToSubject.getId(), "RelationshipToSubject must be persistent.");
        final String subjectId = getRepository().findConnectingSubjectIdAssignedToRelationshipToSubject(relationshipToSubject.getId());
        return subjectRecordService.findByIdWithDirectRelations(subjectId).orElseThrow();
    }

    @Override
    public @NotNull XtdRelationshipType getRelationshipType(XtdRelationshipToSubject relationshipToSubject) {
        Assert.notNull(relationshipToSubject.getId(), "RelationshipToSubject must be persistent.");
        final String relationshipTypeId = getRepository().findRelationshipTypeIdAssignedToRelationshipToSubject(relationshipToSubject.getId());
        return relationshipTypeRecordService.findByIdWithDirectRelations(relationshipTypeId).orElseThrow();
    }


   @Transactional
   @Override
   public @NotNull XtdRelationshipToSubject setRelatedRecords(@NotBlank String relationshipId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds) {

       final XtdRelationshipToSubject relationship = getRepository().findByIdWithDirectRelations(relationshipId).orElseThrow();

       final Iterable<XtdSubject> items = subjectRecordService.findAllEntitiesById(relatedRecordIds);
       final List<XtdSubject> related = StreamSupport
               .stream(items.spliterator(), false)
               .collect(Collectors.toList());

       relationship.getTargetSubjects().clear();
       relationship.getTargetSubjects().addAll(related);

       final XtdRelationshipToSubject persistentRelationship = getRepository().save(relationship);
       log.trace("Updated relationship: {}", persistentRelationship);
       return persistentRelationship;
   }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelationshipToSubject relationshipRecord,
                                     @NotBlank String relatingRecordId) {
        final XtdSubject relatingCatalogRecord = subjectRecordService
                .findByIdWithDirectRelations(relatingRecordId)
                .orElseThrow();
        relationshipRecord.setConnectingSubject(relatingCatalogRecord);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelationshipToSubject relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdSubject> items = subjectRecordService.findAllEntitiesById(relatedRecordIds);
        final List<XtdSubject> related = StreamSupport
                .stream(items.spliterator(), false)
                .collect(Collectors.toList());
        relationshipRecord.getTargetSubjects().clear();
        relationshipRecord.getTargetSubjects().addAll(related);
    }

    @Transactional
    @Override
    public @NotNull XtdRelationshipToSubject setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdRelationshipToSubject relationship = getRepository().findByIdWithDirectRelations(recordId).orElseThrow();

        switch (relationType) {
            case RelationshipType -> {
                if (relationship.getRelationshipType() != null) {
                    throw new IllegalArgumentException("Object already has a relationship type assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one relationship type must be assigned.");
                } else {
                    final XtdRelationshipType relationshipType = relationshipTypeRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    relationship.setRelationshipType(relationshipType);
                }
            }
            case ScopeSubjects -> {
                final Iterable<XtdSubject> subjects = subjectRecordService.findAllEntitiesById(relatedRecordIds);
                final List<XtdSubject> relatedSubjects = StreamSupport
                        .stream(subjects.spliterator(), false)
                        .collect(Collectors.toList());

                relationship.getScopeSubjects().clear();
                relationship.getScopeSubjects().addAll(relatedSubjects);
            }
            case TargetSubjects -> {
                final Iterable<XtdSubject> targetSubjects = subjectRecordService.findAllEntitiesById(relatedRecordIds);
                final List<XtdSubject> relatedTargetSubjects = StreamSupport
                        .stream(targetSubjects.spliterator(), false)
                        .collect(Collectors.toList());

                relationship.getTargetSubjects().clear();
                relationship.getTargetSubjects().addAll(relatedTargetSubjects);
            }
            default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        final XtdRelationshipToSubject persistentRelationship = getRepository().save(relationship);
        log.trace("Updated relationship: {}", persistentRelationship);
        return persistentRelationship;
    }   
}
