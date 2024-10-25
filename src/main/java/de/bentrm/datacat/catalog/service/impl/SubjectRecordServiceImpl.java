package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.RelationshipToSubjectRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class SubjectRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdSubject, SubjectRepository>
        implements SubjectRecordService {

    private final PropertyRepository propertyRepository;
    private final RelationshipToSubjectRepository relationshipToSubjectRepository;
    private final ConceptRecordService conceptRecordService;

    public SubjectRecordServiceImpl(SessionFactory sessionFactory,
            SubjectRepository repository,
            PropertyRepository propertyRepository,
            RelationshipToSubjectRepository relationshipToSubjectRepository,
            ConceptRecordService conceptRecordService,
            CatalogCleanupService cleanupService) {
        super(XtdSubject.class, sessionFactory, repository, cleanupService);
        this.propertyRepository = propertyRepository;
        this.relationshipToSubjectRepository = relationshipToSubjectRepository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Subject;
    }

    @Override
    public List<XtdProperty> getProperties(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> propertyIds = propertyRepository.findAllPropertyIdsAssignedToSubject(subject.getId());
        final Iterable<XtdProperty> properties = propertyRepository.findAllById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdRelationshipToSubject> getConnectedSubjects(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> relationshipIds = relationshipToSubjectRepository
                .findAllConnectedSubjectRelationshipIdsAssignedToSubject(subject.getId());
        final Iterable<XtdRelationshipToSubject> relations = relationshipToSubjectRepository
                .findAllById(relationshipIds);

        return StreamSupport
                .stream(relations.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdRelationshipToSubject> getConnectingSubjects(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> relationshipIds = relationshipToSubjectRepository
                .findAllConnectingSubjectRelationshipIdsAssignedToSubject(subject.getId());
        final Iterable<XtdRelationshipToSubject> relations = relationshipToSubjectRepository
                .findAllById(relationshipIds);

        return StreamSupport
                .stream(relations.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdSubject setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdSubject subject = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Properties:
                final Iterable<XtdProperty> items = propertyRepository.findAllById(relatedRecordIds, 0);
                final List<XtdProperty> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                subject.getProperties().clear();
                subject.getProperties().addAll(related);
                break;
            default:
                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                break;
        }

        final XtdSubject persistentSubject = getRepository().save(subject);
        log.trace("Updated relationship: {}", persistentSubject);
        return persistentSubject;
    }
}
