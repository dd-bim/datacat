package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.*;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.value.CatalogEntryProperties;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogItemSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.dto.CatalogItemStatistics;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Transactional(readOnly = true)
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private HierarchyDao hierarchyDao;

    @Autowired
    private CatalogEntryFactory catalogEntryFactory;

    @Autowired
    private TranslationRespository translationRespository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @Autowired
    private RootRepository rootRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Override
    public CatalogStatistics getStatistics() {
        final Map<String, Long> labelsStats = catalogItemRepository.statistics();
        final CatalogStatistics statistics = new CatalogStatistics();
        labelsStats.forEach((label, count) -> {
            if (label.startsWith("Xtd")) {
                statistics.getItems().add(new CatalogItemStatistics(label, count));
            }
        });
        return statistics;
    }

    @Transactional
    @Override
    public @NotNull CatalogItem createCatalogEntry(CatalogEntryType type, CatalogEntryProperties properties) {
        final CatalogItem catalogEntry = catalogEntryFactory.create(type, properties);
        final CatalogItem persistentCatalogEntry = catalogItemRepository.save(catalogEntry);
        log.trace("Persisted new catalog entry: {}", persistentCatalogEntry);
        return catalogEntry;
    }

    @Transactional
    @Override
    public @NotNull CatalogItem deleteEntry(String id) {
        log.trace("Deleting catalog entry with id {}...", id);
        final CatalogItem entry = catalogItemRepository.findById(id).orElseThrow();

        if (entry instanceof XtdRelationship) {
            throw new NoSuchElementException("Retrieved catalog entry is a catalog relationship.");
        }

        final Consumer<Translation> deleteTranslation = translation -> {
            translationRespository.delete(translation);
            log.trace("Deleted translation of catalog entry {}: {}", id, translation);
        };

        log.trace("Purging name entities...");
        entry.getNames().forEach(deleteTranslation);

        log.trace("Purging description entities...");
        entry.getDescriptions().forEach(deleteTranslation);

        log.trace("Cascading into catalog relationships owned by catalog entry {}...", id);
        final List<XtdRelationship> ownedRelationships = entry.getOwnedRelationships();
        ownedRelationships.forEach(relationship -> {
            relationshipRepository.delete(relationship);
            log.trace("Owned Relationship of catalog entry {} deleted: {}", id, relationship);
        });

        catalogItemRepository.delete(entry);
        log.trace("Catalog item deleted: {}", entry);

        return entry;
    }

    @Transactional
    @Override
    public @NotNull XtdRelationship deleteRelationship(@NotBlank String id) {
        final XtdRelationship relationship = relationshipRepository.findById(id).orElseThrow();

        final Consumer<Translation> deleteTranslation = translation -> {
            translationRespository.delete(translation);
            log.debug("Translation deleted: {}", translation);
        };
        relationship.getNames().forEach(deleteTranslation);
        relationship.getDescriptions().forEach(deleteTranslation);

        relationshipRepository.delete(relationship);
        log.debug("Relationship deleted: {}", relationship);
        return relationship;
    }

    @Transactional
    @Override
    public CatalogItem setVersion(String id, String versionId, String versionDate) {
        Assert.isTrue(versionId != null || versionDate != null, "No valid version provided.");

        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        if (versionId != null) {
            item.setVersionId(versionId);
        }
        if (versionDate != null) {
            item.setVersionDate(versionDate);
        }

        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem addName(String id, String nameId, Locale locale, String value) {
        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        item.addName(nameId, locale, value);
        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem updateName(String id, String nameId, String value) {
        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        item.updateName(nameId, value);
        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem deleteName(String id, String nameId) {
        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        item.deleteName(nameId);
        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem addDescription(String id, String descriptionId, Locale locale, String value) {
        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        item.addDescription(descriptionId, locale, value);
        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem updateDescription(String id, String descriptionId, String value) {
        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        item.updateDescription(descriptionId, value);
        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem deleteDescription(String id, String descriptionId) {
        final CatalogItem item = catalogItemRepository.findById(id).orElseThrow();
        item.deleteDescription(descriptionId);
        return catalogItemRepository.save(item);
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
        final Tag tag = tagRepository.findById(id).orElseThrow();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @NotNull
    @Override
    public @NotNull Tag deleteTag(String id) {
        Assert.notNull(id, "id may not be null.");
        final Tag tag = tagRepository.findById(id).orElseThrow();
        tagRepository.delete(tag);
        return tag;
    }

    @Transactional
    @Override
    public CatalogItem addTag(String entryId, String tagId) {
        CatalogItem item = catalogItemRepository.findById(entryId).orElseThrow();
        final Tag tag = tagRepository.findById(tagId).orElseThrow();
        item.addTag(tag);
        return catalogItemRepository.save(item);
    }

    @Transactional
    @Override
    public CatalogItem removeTag(String entryId, String tagId) {
        final CatalogItem item = catalogItemRepository.findById(entryId).orElseThrow();
        final Tag tag = tagRepository.findById(tagId).orElseThrow();
        item.removeTag(tag);
        return catalogItemRepository.save(item);
    }

    @Override
    public @NotNull List<CatalogItem> getAllEntriesById(List<String> ids) {
        final Iterable<CatalogItem> items = catalogItemRepository.findAllById(ids);
        final List<CatalogItem> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdRoot> getAllRootItemsById(List<String> ids) {
        final Iterable<XtdRoot> items = rootRepository.findAllById(ids);
        final List<XtdRoot> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdObject> getAllObjectsById(List<String> ids) {
        final Iterable<XtdObject> items = objectRepository.findAllById(ids);
        final List<XtdObject> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public @NotNull List<XtdCollection> getAllCollectionsById(List<String> ids) {
        final Iterable<XtdCollection> items = collectionRepository.findAllById(ids);
        final List<XtdCollection> results = new ArrayList<>();
        items.forEach(results::add);
        return results;
    }

    @Override
    public Optional<CatalogItem> getEntryById(String id) {
        return catalogItemRepository.findById(id);
    }

    @Override
    public Optional<XtdRoot> getRootItem(String id) {
        return rootRepository.findById(id);
    }

    @Override
    public Optional<XtdObject> getObject(String id) {
        return objectRepository.findById(id);
    }

    @Override
    public Optional<XtdCollection> getCollection(String id) {
        return collectionRepository.findById(id);
    }

    @Override
    public Optional<XtdRelationship> getRelationship(String id) {
        return relationshipRepository.findById(id);
    }

    @Override
    public Page<CatalogItem> findAllCatalogItems(CatalogItemSpecification specification) {
        Collection<CatalogItem> catalogItems;
        Pageable pageable;
        final long count = countCatalogItems(specification);
        final Session session = sessionFactory.openSession();

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                catalogItems = session.loadAll(CatalogItem.class, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                catalogItems = session.loadAll(CatalogItem.class, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            catalogItems = session.loadAll(CatalogItem.class, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(catalogItems), pageable, () -> count);
//        // TODO: Quickfix
//        final List<String> ids = all.getContent().stream().map(CatalogItem::getId).collect(Collectors.toList());
//        final List<CatalogItem> pageContent = new ArrayList<>();
//        final Iterable<CatalogItem> reloaded = catalogItemRepository.findAllById(ids);
//        reloaded.forEach(pageContent::add);
//        return PageableExecutionUtils.getPage(pageContent, all.getPageable(), all::getTotalElements);
    }

    @Override
    public long countCatalogItems(CatalogItemSpecification specification) {
        final Session session = sessionFactory.openSession();
        return session.count(CatalogItem.class, specification.getFilters());
    }

    @Override
    public long countRootItems(RootSpecification specification) {
        final Session session = sessionFactory.openSession();
        return session.count(XtdRoot.class, specification.getFilters());
    }

    @Override
    public HierarchyValue getHierarchy(CatalogItemSpecification rootNodeSpecification, int depth) {
        final Page<CatalogItem> rootNodes = findAllCatalogItems(rootNodeSpecification);
        final List<String> rootNodeIds = rootNodes.map(CatalogItem::getId).stream().collect(Collectors.toList());

        final List<List<String>> paths = hierarchyDao.getHierarchyPaths(rootNodeIds, depth);

        final Set<String> nodeIds = new HashSet<>();
        paths.forEach(nodeIds::addAll);

        final Iterable<CatalogItem> nodes = catalogItemRepository.findAllById(nodeIds);
        List<CatalogItem> leaves = new ArrayList<>();
        nodes.forEach(leaves::add);

        return new HierarchyValue(leaves, paths);
    }
}
