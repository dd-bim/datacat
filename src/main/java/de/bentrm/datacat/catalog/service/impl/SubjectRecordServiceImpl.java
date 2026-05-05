package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.PropertiesDtoProjection;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.RelationshipToSubjectRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class SubjectRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdSubject, SubjectRepository>
        implements SubjectRecordService {

    @Autowired
    private PropertyRecordService propertyRecordService;

    @Autowired
    private RelationshipToSubjectRecordService relationshipToSubjectRecordService;

    @Autowired
    private ConceptRecordService conceptRecordService;

    public SubjectRecordServiceImpl(Neo4jTemplate neo4jTemplate,
            SubjectRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdSubject.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull Page<XtdSubject> findAll(@NotNull QuerySpecification specification) {
        // Standard-Implementierung mit lazy loading
        // Die GraphQL BatchMappings laden Relationen gezielt bei Bedarf über getProperties(), 
        // getConnectedSubjects() und getConnectingSubjects()
        return super.findAll(specification);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Subject;
    }

    @Override
    public @NotNull Optional<XtdSubject> findById(@NotNull String id) {
        // Überschreibe die Standard-Methode, um KEINE Relationen zu laden
        return getRepository().findByIdWithoutRelations(id);
    }

    @Override
    public List<XtdProperty> getProperties(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> propertyIds = getRepository().findAllPropertyIdsAssignedToSubject(subject.getId());
        final Iterable<XtdProperty> properties = propertyRecordService.findAllEntitiesById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdRelationshipToSubject> getConnectedSubjects(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> relationshipIds = getRepository()
                .findAllConnectedSubjectRelationshipIdsAssignedToSubject(subject.getId());
        final Iterable<XtdRelationshipToSubject> relations = relationshipToSubjectRecordService
                .findAllEntitiesById(relationshipIds);

        return StreamSupport
                .stream(relations.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdRelationshipToSubject> getConnectingSubjects(XtdSubject subject) {
        Assert.notNull(subject.getId(), "Subject must be persistent.");
        final List<String> relationshipIds = getRepository()
                .findAllConnectingSubjectRelationshipIdsAssignedToSubject(subject.getId());
        final Iterable<XtdRelationshipToSubject> relations = relationshipToSubjectRecordService
                .findAllEntitiesById(relationshipIds);

        return StreamSupport
                .stream(relations.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<XtdSubject> findByIdWithIncomingAndOutgoingRelations(String id) {
        return getRepository().findByIdWithIncomingAndOutgoingRelations(id);
    }

    @Transactional
    @Override
    public @NotNull XtdSubject setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdSubject subject = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            case Properties -> {
                final Iterable<XtdProperty> items = propertyRecordService.findAllEntitiesById(relatedRecordIds);
                final List<XtdProperty> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                subject.getProperties().clear();
                subject.getProperties().addAll(related);
            }
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        neo4jTemplate.saveAs(subject, PropertiesDtoProjection.class);
        log.trace("Updated relationship: {}", subject);
        return subject;
    }
}
