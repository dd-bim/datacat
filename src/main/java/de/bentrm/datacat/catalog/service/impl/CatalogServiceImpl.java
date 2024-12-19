package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.dto.CatalogRecordStatistics;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private RelationshipRepository relationshipRepository;

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
        if (id != null) tag.setId(id);
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public @NotNull Tag updateTag(String id, String name) {
        final Tag tag = tagRepository.findByIdWithDirectRelations(id).orElseThrow();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @NotNull
    @Override
    public @NotNull Tag deleteTag(String id) {
        Assert.notNull(id, "id may not be null.");
        final Tag tag = tagRepository.findByIdWithDirectRelations(id).orElseThrow();
        tagRepository.delete(tag);
        return tag;
    }

    @Transactional
    @Override
    public CatalogRecord addTag(String entryId, String tagId) {
        CatalogRecord item = catalogRecordRepository.findByIdWithDirectRelations(entryId).orElseThrow();
        final Tag tag = tagRepository.findByIdWithDirectRelations(tagId).orElseThrow();
        item.addTag(tag);
        return catalogRecordRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogRecord removeTag(String entryId, String tagId) {
        final CatalogRecord item = catalogRecordRepository.findByIdWithDirectRelations(entryId).orElseThrow();
        final Tag tag = tagRepository.findByIdWithDirectRelations(tagId).orElseThrow();
        item.removeTag(tag);
        return catalogRecordRepository.save(item);
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
    public Optional<XtdRoot> getRootItem(String id) {
        return rootRepository.findByIdWithDirectRelations(id);
    }

    @Override
    public Optional<XtdObject> getObject(String id) {
        return objectRepository.findByIdWithDirectRelations(id);
    }

    @Override
    public Optional<XtdConcept> getConcept(String id) {
        return conceptRepository.findByIdWithDirectRelations(id);
    }

    @Override
    public Optional<AbstractRelationship> getRelationship(String id) {
        return relationshipRepository.findByIdWithDirectRelations(id);
    }
    
    @Override
    public Page<CatalogRecord> findAllCatalogRecords(CatalogRecordSpecification specification) {
        Collection<CatalogRecord> catalogRecords;
        Pageable pageable;
        final long count = countCatalogRecords(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            // final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                // catalogRecords = session.loadAll(CatalogRecord.class, specification.getFilters(), pagination);
                catalogRecords = neo4jTemplate.findAll(CatalogRecord.class);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                // final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                // catalogRecords = session.loadAll(CatalogRecord.class, specification.getFilters(), sortOrder, pagination);
                catalogRecords = neo4jTemplate.findAll(CatalogRecord.class);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            // catalogRecords = session.loadAll(CatalogRecord.class, specification.getFilters());
            catalogRecords = neo4jTemplate.findAll(CatalogRecord.class);
        }

        return PageableExecutionUtils.getPage(List.copyOf(catalogRecords), pageable, () -> count);
    }

    @Override
    public long countCatalogRecords(CatalogRecordSpecification specification) {
        // return session.count(CatalogRecord.class, specification.getFilters());
        return neo4jTemplate.count(CatalogRecord.class);
    }

    @Override
    public long countRootItems(RootSpecification specification) {
        // return session.count(XtdRoot.class, specification.getFilters());
        return neo4jTemplate.count(XtdRoot.class);
    }

    @Override
    public HierarchyValue getHierarchy(CatalogRecordSpecification rootNodeSpecification, int depth) {
        final Page<CatalogRecord> rootNodes = findAllCatalogRecords(rootNodeSpecification);
        final List<String> rootNodeIds = rootNodes.map(CatalogRecord::getId).stream().collect(Collectors.toList());

        final List<List<String>> paths = rootRepository.findRelationshipPaths(rootNodeIds);
        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<XtdRoot> nodes = rootRepository.findAllEntitiesById(nodeIds);
        final List<XtdRoot> leaves = StreamSupport
                .stream(nodes.spliterator(), false)
                .collect(Collectors.toList());

        return new HierarchyValue(leaves, paths);
    }
}
