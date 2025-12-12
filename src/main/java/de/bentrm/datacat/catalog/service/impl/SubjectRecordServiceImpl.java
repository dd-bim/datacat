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
        // Use optimized query when no complex filters are applied
        if (isSimpleQuery(specification)) {
            List<XtdSubject> subjects = findSubjectsWithRelations(specification);
            Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
            return PageableExecutionUtils.getPage(subjects, pageable, 
                () -> getRepository().count());
        }
        // Fallback to default implementation for complex queries
        return super.findAll(specification);
    }

    private boolean isSimpleQuery(QuerySpecification specification) {
        // Consider it simple if there are no filters, only pagination and sorting
        return specification.getFilters() == null || specification.getFilters().isEmpty();
    }

    private List<XtdSubject> findSubjectsWithRelations(QuerySpecification specification) {
        Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
        String query = buildOptimizedSubjectQuery(pageable);
        return getNeo4jTemplate().findAll(query, XtdSubject.class);
    }

    private String buildOptimizedSubjectQuery(Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("MATCH (s:XtdSubject) ");
        
        // Add optional matches for commonly used relations
        queryBuilder.append("OPTIONAL MATCH (s)-[:COLLECTS]->(collectedSubjects:XtdSubject) ");
        queryBuilder.append("OPTIONAL MATCH (s)<-[:COLLECTS]-(collectingSubjects:XtdSubject) ");
        queryBuilder.append("OPTIONAL MATCH (s)-[:SPECIALIZES]->(specializedSubjects:XtdSubject) ");
        queryBuilder.append("OPTIONAL MATCH (s)<-[:SPECIALIZES]-(specializingSubjects:XtdSubject) ");
        queryBuilder.append("OPTIONAL MATCH (s)-[:GROUPS]->(groupedObjects:XtdObject) ");
        queryBuilder.append("OPTIONAL MATCH (s)-[:TAGGED]->(tags:Tag) ");
        
        queryBuilder.append("RETURN s, ");
        queryBuilder.append("collect(DISTINCT collectedSubjects) as collectedSubjects, ");
        queryBuilder.append("collect(DISTINCT collectingSubjects) as collectingSubjects, ");
        queryBuilder.append("collect(DISTINCT specializedSubjects) as specializedSubjects, ");
        queryBuilder.append("collect(DISTINCT specializingSubjects) as specializingSubjects, ");
        queryBuilder.append("collect(DISTINCT groupedObjects) as groupedObjects, ");
        queryBuilder.append("collect(DISTINCT tags) as tags ");
        
        // Add sorting if specified
        if (pageable.getSort().isSorted()) {
            queryBuilder.append("ORDER BY ");
            String sortClause = pageable.getSort().stream()
                    .map(order -> "s." + order.getProperty() + " " + order.getDirection())
                    .collect(Collectors.joining(", "));
            queryBuilder.append(sortClause).append(" ");
        }
        
        // Add pagination
        queryBuilder.append("SKIP ").append(pageable.getOffset()).append(" ");
        queryBuilder.append("LIMIT ").append(pageable.getPageSize());
        
        return queryBuilder.toString();
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
