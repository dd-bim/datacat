package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.RelationshipToSubjectRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.repository.RelationshipTypeRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    private final SubjectRepository subjectRepository;
    private final RelationshipTypeRepository relationshipTypeRepository;
    private final ObjectRecordService objectRecordService;

    public RelationshipToSubjectRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     RelationshipToSubjectRepository repository,
                                     SubjectRepository subjectRepository,
                                     RelationshipTypeRepository relationshipTypeRepository,
                                     ObjectRecordService objectRecordService,
                                     CatalogCleanupService cleanupService) {
        super(XtdRelationshipToSubject.class, neo4jTemplate, repository, cleanupService);
        this.subjectRepository = subjectRepository;
        this.relationshipTypeRepository = relationshipTypeRepository;
        this.objectRecordService = objectRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipToSubject;
    }

    @Override
    public @NotNull List<XtdSubject> getScopeSubjects(XtdRelationshipToSubject relationshipToSubject) {
        Assert.notNull(relationshipToSubject.getId(), "RelationshipToSubject must be persistent.");
        final List<String> subjectIds = subjectRepository.findAllScopeSubjectIdsAssignedToRelationshipToSubject(relationshipToSubject.getId());
        final Iterable<XtdSubject> subjects = subjectRepository.findAllById(subjectIds);

        return StreamSupport
                .stream(subjects.spliterator(), false)
                .collect(Collectors.toList());
    }

   @Transactional
   @Override
   public @NotNull XtdRelationshipToSubject setRelatedRecords(@NotBlank String relationshipId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds) {

       final XtdRelationshipToSubject relationship = getRepository().findById(relationshipId).orElseThrow();

       final Iterable<XtdSubject> items = subjectRepository.findAllById(relatedRecordIds);
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
        final XtdSubject relatingCatalogRecord = subjectRepository
                .findById(relatingRecordId)
                .orElseThrow();
        relationshipRecord.setConnectingSubject(relatingCatalogRecord);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelationshipToSubject relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdSubject> items = subjectRepository.findAllById(relatedRecordIds);
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

        final XtdRelationshipToSubject relationship = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case RelationshipType -> {
                if (relationship.getRelationshipType() != null) {
                    throw new IllegalArgumentException("Object already has a relationship type assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one relationship type must be assigned.");
                } else {
                    final XtdRelationshipType relationshipType = relationshipTypeRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    relationship.setRelationshipType(relationshipType);
                }
            }
            case ScopeSubjects -> {
                final Iterable<XtdSubject> subjects = subjectRepository.findAllById(relatedRecordIds);
                final List<XtdSubject> relatedSubjects = StreamSupport
                        .stream(subjects.spliterator(), false)
                        .collect(Collectors.toList());

                relationship.getScopeSubjects().clear();
                relationship.getScopeSubjects().addAll(relatedSubjects);
            }
            case TargetSubjects -> {
                final Iterable<XtdSubject> targetSubjects = subjectRepository.findAllById(relatedRecordIds);
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
