package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.dto.TagDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.CatalogRecordDtoProjection;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.dto.CatalogRecordStatistics;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Validated
@Transactional(readOnly = true)
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    Neo4jTemplate neo4jTemplate;

    @Autowired
    Neo4jClient neo4jClient;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CatalogRecordRepository catalogRecordRepository;

    @Autowired
    private RootRepository rootRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ConceptRepository conceptRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private ExternalDocumentRepository externalDocumentRepository;

    @Override
    public CatalogStatistics getStatistics() {
        final List<CatalogRecordStatistics> labelsStats = catalogRecordRepository.statistics();

        final CatalogStatistics statistics = new CatalogStatistics();
        labelsStats.forEach(record -> {
            if (record.getId().startsWith("Xtd")) {
                statistics.getItems().add(record);
            }
        });
        return statistics;
    }

    @Transactional
    @Override
    public @NotNull Tag createTag(String id, String name) {
        final Tag tag = new Tag();
        if (id != null)
            tag.setId(id);
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public @NotNull Tag updateTag(String id, String name) {
        final Tag tag = tagRepository.findByIdWithDirectRelations(id).orElseThrow(() -> new IllegalArgumentException("No tag with id " + id + " found."));
        tag.setName(name);
        neo4jTemplate.saveAs(tag, TagDtoProjection.class);
        return tag;
    }

    @Transactional
    @Override
    public @NotNull Tag deleteTag(String id) {
        Assert.notNull(id, "id may not be null.");
        final Tag tag = tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No tag with id " + id + " found."));
        tagRepository.deleteById(id);
        return tag;
    }

    @Transactional
    @Override
    public CatalogRecord addTag(String entryId, String tagId) {
        CatalogRecord item = catalogRecordRepository.findByIdWithDirectRelations(entryId).orElseThrow(() -> new IllegalArgumentException("No record with id " + entryId + " found."));
        final Tag tag = tagRepository.findByIdWithDirectRelations(tagId).orElseThrow(() -> new IllegalArgumentException("No record with tag " + tagId + " found."));
        item.addTag(tag);
        neo4jTemplate.saveAs(item, CatalogRecordDtoProjection.class);
        return item;
    }

    @Transactional
    @Override
    public CatalogRecord removeTag(String entryId, String tagId) {
        final CatalogRecord item = catalogRecordRepository.findByIdWithDirectRelations(entryId).orElseThrow(() -> new IllegalArgumentException("No record with id " + entryId + " found."));
        final Tag tag = tagRepository.findByIdWithDirectRelations(tagId).orElseThrow(() -> new IllegalArgumentException("No record with tag " + tagId + " found."));
        item.removeTag(tag);
        neo4jTemplate.saveAs(item, CatalogRecordDtoProjection.class);
        return item;
    }

    @Override
    public @NotNull List<CatalogRecord> getAllEntriesById(List<String> ids) {
        final Iterable<CatalogRecord> items = catalogRecordRepository.findAllEntitiesById(ids);
        final List<CatalogRecord> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdRoot> getAllRootItemsById(List<String> ids) {
        final Iterable<XtdRoot> items = rootRepository.findAllEntitiesById(ids);
        final List<XtdRoot> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdText> getAllTextsById(List<String> ids) {
        final Iterable<XtdText> items = textRepository.findAllEntitiesById(ids);
        final List<XtdText> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdObject> getAllObjectsById(List<String> ids) {
        final Iterable<XtdObject> items = objectRepository.findAllEntitiesById(ids);
        final List<XtdObject> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdConcept> getAllConceptsById(List<String> ids) {
        final Iterable<XtdConcept> items = conceptRepository.findAllEntitiesById(ids);
        final List<XtdConcept> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdExternalDocument> getAllExternalDocumentsById(List<String> ids) {
        final Iterable<XtdExternalDocument> items = externalDocumentRepository.findAllEntitiesById(ids);
        final List<XtdExternalDocument> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public Optional<CatalogRecord> getEntryById(String id) {
        return catalogRecordRepository.findByIdWithDirectRelations(id);
    }

    @Override
    public Optional<XtdObject> getObject(String id) {
        return objectRepository.findByIdWithDirectRelations(id);
    }

    @Override
    public @NotNull String getRelationshipBetweenObjects(String fromId, String toId) {
        // return objectRepository.findRelationshipBetweenObjects(fromId, toId);
        String relationship = objectRepository.findRelationshipBetweenObjects(fromId, toId);
        if (relationship == null) {
            throw new NoSuchElementException("Relationship not found between the given objects:" + fromId
                        + " and " + toId);
        }
        return relationship;
    }

    @Override
    public Long countTargetRelationships(String objectId) {
        return objectRepository.countTargetRelationships(objectId);
    }

    @Override
    public Page<XtdObject> findAllCatalogRecords(CatalogRecordSpecification specification) {
        Collection<XtdObject> catalogRecords;
        Pageable pageable;
        final Long count = countCatalogRecords(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            if (pageable.getSort().isUnsorted()) {
                catalogRecords = neo4jTemplate.findAll(getQuery(specification, pageable), XtdObject.class);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                catalogRecords = neo4jTemplate.findAll(getQuery(specification, pageable, direction, properties),
                XtdObject.class);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            catalogRecords = neo4jTemplate.findAll(getQuery(specification, pageable), XtdObject.class);
        }

        return PageableExecutionUtils.getPage(List.copyOf(catalogRecords), pageable, () -> count);
    }

    @Override
    public @NotNull Long countCatalogRecords(@NotNull CatalogRecordSpecification specification) {
        String query;
        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:XtdObject) RETURN count(n)";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:XtdObject) " + whereClause + " RETURN count(n)";
        }
        return neo4jTemplate.count(query);
    }

    @Override
    public HierarchyValue getHierarchy(@NotNull CatalogRecordSpecification rootNodeSpecification) {
        long startTime = System.currentTimeMillis();
        
        final Page<XtdObject> rootNodes = findAllCatalogRecords(rootNodeSpecification);
        final List<String> rootNodeIds = rootNodes.map(XtdObject::getId).stream().collect(Collectors.toList());
        
        long rootNodesTime = System.currentTimeMillis();
        log.debug("Found {} root nodes in {} ms", rootNodeIds.size(), rootNodesTime - startTime);

        final List<List<String>> paths = findRelationshipPaths(rootNodeIds);
        
        long pathsTime = System.currentTimeMillis();
        log.debug("Found {} paths in {} ms", paths.size(), pathsTime - rootNodesTime);

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);
        
        log.debug("Need to load {} unique nodes from {} paths", nodeIds.size(), paths.size());

        final Iterable<XtdRoot> nodes = rootRepository.findAllEntitiesById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport.stream(nodes.spliterator(), false).collect(Collectors.toList());
        
        long nodesTime = System.currentTimeMillis();
        log.debug("Loaded {} nodes in {} ms", leaves.size(), nodesTime - pathsTime);
        
        long totalTime = System.currentTimeMillis();
        log.info("Hierarchy query completed in {} ms total (root: {}ms, paths: {}ms, nodes: {}ms)", 
                totalTime - startTime, rootNodesTime - startTime, pathsTime - rootNodesTime, nodesTime - pathsTime);

        return new HierarchyValue(leaves, paths);
    }

    public String getQuery(QuerySpecification specification, Pageable pageable) {
        return (getQuery(specification, pageable, null, null));
    }

    public String getQuery(QuerySpecification specification, Pageable pageable, Sort.Direction direction,
            String[] properties) {
        String query;
        String sort = "";
        if (direction != null && properties != null) {
            List<String> prefixedProperties = Arrays.stream(properties)
                    .map(property -> "n.`" + property + "` " + direction.name()).collect(Collectors.toList());
            sort = " ORDER BY " + String.join(", ", prefixedProperties);
        }

        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:XtdObject)" + sort + " RETURN n";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:XtdObject) " + whereClause + sort + " RETURN n";
        }
        query = query + " SKIP " + pageable.getOffset() + " LIMIT " + pageable.getPageSize();

        return query;
    }

    public List<List<String>> findRelationshipPaths(List<String> startIds) {
        return neo4jClient.query("""
                MATCH (start:XtdObject)
                WHERE start.id IN $startIds
                MATCH path = (start)-[*]->(end:XtdObject)
                WHERE NONE(n IN nodes(path)[1..] WHERE (n:XtdInterval OR n:XtdExternalDocument))
                WITH nodes(path) AS nodelist
                WITH [n IN nodelist WHERE NOT (n:XtdRelationshipToSubject OR n:XtdOrderedValue OR n:XtdRelationshipType OR n:XtdRelationshipToProperty OR n:XtdQuantityKind OR n:XtdCountry OR n:XtdDimension OR n:XtdSubdivision) | n.id] AS paths
                RETURN paths
            """)
            .bind(startIds).to("startIds")
            .fetch()
            .all()
            .stream()
            .map(record -> Optional.ofNullable(record.get("paths"))
                               .filter(List.class::isInstance)
                               .map(List.class::cast)
                               .orElse(List.of()))
        .map(list -> ((List<?>) list).stream()
                                     .filter(String.class::isInstance)
                                     .map(String.class::cast)
                                     .toList())
            .toList();
    }

    @Override
    public List<Tag> getTags(String entryId) {
        List<Tag> t = catalogRecordRepository.findTagsByCatalogRecordId(entryId);
        return t;

    }
}
