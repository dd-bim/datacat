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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Transactional(readOnly = true)
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    Neo4jTemplate neo4jTemplate;

    @Autowired
    Neo4jClient neo4jClient;

    // Cache for entity classes to avoid repeated reflection lookups
    private static final Map<String, Class<? extends XtdRoot>> ENTITY_CLASS_CACHE = new HashMap<>();

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
        final Tag tag = tagRepository.findByIdWithDirectRelations(id)
                .orElseThrow(() -> new IllegalArgumentException("No tag with id " + id + " found."));
        tag.setName(name);
        neo4jTemplate.saveAs(tag, TagDtoProjection.class);
        return tag;
    }

    @Transactional
    @Override
    public @NotNull Tag deleteTag(String id) {
        Assert.notNull(id, "id may not be null.");
        final Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No tag with id " + id + " found."));
        tagRepository.deleteById(id);
        return tag;
    }

    @Transactional
    @Override
    public CatalogRecord addTag(String entryId, String tagId) {
        CatalogRecord item = catalogRecordRepository.findByIdWithDirectRelations(entryId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + entryId + " found."));
        final Tag tag = tagRepository.findByIdWithDirectRelations(tagId)
                .orElseThrow(() -> new IllegalArgumentException("No record with tag " + tagId + " found."));
        item.addTag(tag);
        neo4jTemplate.saveAs(item, CatalogRecordDtoProjection.class);
        return item;
    }

    @Transactional
    @Override
    public CatalogRecord removeTag(String entryId, String tagId) {
        final CatalogRecord item = catalogRecordRepository.findByIdWithDirectRelations(entryId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + entryId + " found."));
        final Tag tag = tagRepository.findByIdWithDirectRelations(tagId)
                .orElseThrow(() -> new IllegalArgumentException("No record with tag " + tagId + " found."));
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
            throw new NoSuchElementException(
                    "Relationship not found between the given objects:" + fromId + " and " + toId);
        }
        return relationship;
    }

    @Override
    public @NotNull String getRelationshipBetweenObjectsByName(String fromId, String toId, String name) {
        List<String> relationshipIds = objectRepository.findAllRelationshipsBetweenObjects(fromId, toId);
        
        if (relationshipIds == null || relationshipIds.isEmpty()) {
            throw new NoSuchElementException(
                    "No relationship found between the given objects: " + fromId + " and " + toId);
        }

        // Wenn kein Name angegeben wurde
        if (name == null || name.isEmpty()) {
            if (relationshipIds.size() > 1) {
                throw new IllegalStateException(
                    "Multiple relationships found between objects " + fromId + " and " + toId + 
                    ". Please specify a relationship name to disambiguate.");
            }
            return relationshipIds.get(0);
        }

        // Filtere nach Name
        for (String relId : relationshipIds) {
            Optional<CatalogRecord> relOpt = catalogRecordRepository.findByIdWithDirectRelations(relId);
            if (relOpt.isPresent() && relOpt.get() instanceof de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject) {
                de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject rel = 
                    (de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject) relOpt.get();
                
                de.bentrm.datacat.catalog.domain.XtdRelationshipType relType = rel.getRelationshipType();
                if (relType != null) {
                    // Lade vollständig mit Neo4jTemplate um names zu bekommen
                    relType = neo4jTemplate.findById(relType.getId(), de.bentrm.datacat.catalog.domain.XtdRelationshipType.class).orElse(null);
                    if (relType != null && relType.getNames() != null) {
                        for (de.bentrm.datacat.catalog.domain.XtdMultiLanguageText multiLangText : relType.getNames()) {
                            if (multiLangText.getTexts() != null) {
                                for (de.bentrm.datacat.catalog.domain.XtdText text : multiLangText.getTexts()) {
                                    if (name.equals(text.getText())) {
                                        return relId;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        throw new NoSuchElementException(
            "No relationship found between objects " + fromId + " and " + toId + " with name '" + name + "'");
    }

    @Override
    public Long countTargetRelationships(String objectId) {
        return objectRepository.countTargetRelationships(objectId);
    }

    @Override
    public List<String> findAllCatalogRecords(CatalogRecordSpecification specification) {
        long startTime = System.currentTimeMillis();

        // Match any node (n) and use filters for label checks
        // Filters already contain label constraints like "n:XtdSubject"
        String baseMatch = "MATCH (n)";
        String whereClause = specification.getFilters().isEmpty() ? ""
                : " WHERE " + String.join(" AND ", specification.getFilters());

        String query = baseMatch + whereClause + " RETURN n.id AS id";
        log.debug("findAllCatalogRecords query: {}", query);

        List<String> result = neo4jClient.query(query).fetch().all().stream().map(record -> (String) record.get("id"))
                .collect(Collectors.toList());

        long duration = System.currentTimeMillis() - startTime;
        log.debug("findAllCatalogRecords completed: {} IDs in {}ms", result.size(), duration);
        return result;
    }

    @Override
    public HierarchyValue getHierarchy(@NotNull CatalogRecordSpecification rootNodeSpecification) {
        long hierarchyStartTime = System.currentTimeMillis();
        log.debug("=== Starting getHierarchy ===");

        // Step 1: Find root node IDs
        long step1Start = System.currentTimeMillis();
        final List<String> rootNodeIds = findAllCatalogRecords(rootNodeSpecification);
        long step1Duration = System.currentTimeMillis() - step1Start;
        log.debug("Step 1: Found {} root node IDs in {}ms", rootNodeIds.size(), step1Duration);

        if (rootNodeIds.isEmpty()) {
            log.warn("No root nodes found for specification: {}", rootNodeSpecification);
            long totalDuration = System.currentTimeMillis() - hierarchyStartTime;
            log.debug("=== getHierarchy completed in {}ms (empty result) ===", totalDuration);
            return new HierarchyValue(List.of(), List.of());
        }

        // Step 2: Find relationship paths
        long step2Start = System.currentTimeMillis();
        final List<List<String>> paths = findRelationshipPaths(rootNodeIds);
        long step2Duration = System.currentTimeMillis() - step2Start;
        log.debug("Step 2: Found {} paths from root nodes in {}ms", paths.size(), step2Duration);

        // Step 3: Collect unique node IDs from paths
        long step3Start = System.currentTimeMillis();
        final Set<String> nodeIds = paths.stream().flatMap(List::stream).collect(Collectors.toSet());
        long step3Duration = System.currentTimeMillis() - step3Start;
        log.debug("Step 3: Collected {} unique node IDs from paths in {}ms", nodeIds.size(), step3Duration);

        // Step 4: Load entities in batches
        long step4Start = System.currentTimeMillis();
        final int BATCH_SIZE = 500;
        final List<String> nodeIdList = new ArrayList<>(nodeIds);
        final List<XtdRoot> leaves = new ArrayList<>();

        String query = """
                MATCH (node:XtdRoot)
                WHERE node.id IN $ids
                RETURN node.id AS id, labels(node) AS labels
                """;

        int totalBatches = (nodeIdList.size() + BATCH_SIZE - 1) / BATCH_SIZE;
        log.debug("Step 4: Loading {} node IDs in {} batches of max {} IDs each...", nodeIdList.size(), totalBatches,
                BATCH_SIZE);

        for (int i = 0; i < nodeIdList.size(); i += BATCH_SIZE) {
            long batchStart = System.currentTimeMillis();
            int batchNum = (i / BATCH_SIZE) + 1;
            int end = Math.min(i + BATCH_SIZE, nodeIdList.size());
            List<String> batch = nodeIdList.subList(i, end);

            List<XtdRoot> batchResults = neo4jClient.query(query).bind(batch).to("ids").fetchAs(XtdRoot.class)
                    .mappedBy((typeSystem, record) -> {
                        String id = record.get("id").asString();
                        List<String> labels = record.get("labels").asList(org.neo4j.driver.Value::asString);

                        // Exclude abstract base classes and find the most specific concrete class
                        // Abstract classes: Entity, CatalogRecord, XtdRoot, XtdObject, XtdConcept
                        String specificLabel = labels
                                .stream().filter(l -> !l.equals("Entity") && !l.equals("CatalogRecord")
                                        && !l.equals("XtdRoot") && !l.equals("XtdObject") && !l.equals("XtdConcept"))
                                .findFirst().orElse(null);

                        if (specificLabel == null) {
                            log.warn("No concrete label found for id {}, labels: {}", id, labels);
                            return null;
                        }

                        // Create instance of the specific concrete type
                        XtdRoot entity = createEntityInstance(specificLabel);
                        if (entity != null) {
                            entity.setId(id);
                            entity.setTags(new HashSet<>()); // Empty tags - GraphQL will use BatchMapping
                        }
                        return entity;
                    }).all().stream().filter(entity -> entity != null).toList();

            leaves.addAll(batchResults);
            long batchDuration = System.currentTimeMillis() - batchStart;
            log.debug("  Batch {}/{}: loaded {} entities in {}ms", batchNum, totalBatches, batchResults.size(),
                    batchDuration);
        }

        long step4Duration = System.currentTimeMillis() - step4Start;
        log.debug("Step 4 completed: Loaded {} entities in {}ms across {} batches (WITHOUT tags)", leaves.size(),
                step4Duration, totalBatches);

        long totalDuration = System.currentTimeMillis() - hierarchyStartTime;
        log.debug("=== getHierarchy completed in {}ms (Step1: {}ms, Step2: {}ms, Step3: {}ms, Step4: {}ms) ===",
                totalDuration, step1Duration, step2Duration, step3Duration, step4Duration);

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
        long startTime = System.currentTimeMillis();
        log.debug("findRelationshipPaths: Finding paths for {} start IDs: {}", startIds.size(),
                startIds.size() <= 10 ? startIds : startIds.subList(0, 10) + "...");

        List<List<String>> result = neo4jClient
                .query("""
                            MATCH (start:XtdObject)
                            WHERE start.id IN $startIds
                            MATCH path = (start)-[*1..8]->(end:XtdObject)
                            WHERE NONE(n IN nodes(path)[1..] WHERE (n:XtdInterval OR n:XtdExternalDocument))
                            WITH nodes(path) AS nodelist
                            WITH [n IN nodelist WHERE NOT (n:XtdRelationshipToSubject OR n:XtdOrderedValue OR n:XtdRelationshipType OR n:XtdRelationshipToProperty OR n:XtdQuantityKind OR n:XtdCountry OR n:XtdDimension OR n:XtdSubdivision) | n.id] AS paths
                            RETURN paths
                        """)
                .bind(startIds).to("startIds").fetch().all().stream()
                .map(record -> Optional.ofNullable(record.get("paths")).filter(List.class::isInstance)
                        .map(List.class::cast).orElse(List.of()))
                .map(list -> ((List<?>) list).stream().filter(String.class::isInstance).map(String.class::cast)
                        .toList())
                .toList();

        long duration = System.currentTimeMillis() - startTime;
        int totalNodes = result.stream().mapToInt(List::size).sum();
        log.debug("findRelationshipPaths completed: {} paths with {} total nodes in {}ms", result.size(), totalNodes,
                duration);
        return result;
    }

    /**
     * Creates a minimal entity instance based on the Neo4j label. Returns an
     * instance with only the ID set, no relationships loaded. Uses caching to avoid
     * repeated reflection lookups.
     */
    @SuppressWarnings("unchecked")
    private XtdRoot createEntityInstance(String label) {
        try {
            // Check cache first
            Class<? extends XtdRoot> clazz = ENTITY_CLASS_CACHE.get(label);

            if (clazz == null) {
                // Map label to fully qualified class name
                String className = "de.bentrm.datacat.catalog.domain." + label;
                Class<?> loadedClass = Class.forName(className);

                // Check if it's a subclass of XtdRoot
                if (XtdRoot.class.isAssignableFrom(loadedClass)) {
                    clazz = (Class<? extends XtdRoot>) loadedClass;
                    ENTITY_CLASS_CACHE.put(label, clazz);
                } else {
                    log.warn("Label {} does not correspond to XtdRoot subclass", label);
                    return null;
                }
            }

            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to create entity instance for label: {}", label, e);
            return null;
        }
    }

    @Override
    public List<Tag> getTags(String entryId) {
        List<Tag> t = catalogRecordRepository.findTagsByCatalogRecordId(entryId);
        return t;
    }

    @Override
    public Map<String, List<Tag>> getTagsForMultipleIds(List<String> recordIds) {
        long startTime = System.currentTimeMillis();

        if (recordIds == null || recordIds.isEmpty()) {
            log.debug("getTagsForMultipleIds: No record IDs provided");
            return Map.of();
        }

        log.debug("getTagsForMultipleIds: Loading tags for {} record IDs...", recordIds.size());

        // Load all tags for all records in ONE query
        Collection<Map<String, Object>> results = neo4jClient.query("""
                    MATCH (record:CatalogRecord)-[:TAGGED]->(tag:Tag)
                    WHERE record.id IN $ids
                    RETURN record.id AS recordId, tag.id AS tagId, tag.name AS tagName
                """).bind(recordIds).to("ids").fetch().all();

        // Group tags by record ID
        Map<String, List<Tag>> tagsMap = new HashMap<>();

        for (Map<String, Object> row : results) {
            String recordId = (String) row.get("recordId");
            Tag tag = new Tag();
            tag.setId((String) row.get("tagId"));
            tag.setName((String) row.get("tagName"));

            tagsMap.computeIfAbsent(recordId, k -> new ArrayList<>()).add(tag);
        }

        long duration = System.currentTimeMillis() - startTime;
        int totalTags = tagsMap.values().stream().mapToInt(List::size).sum();
        log.debug("getTagsForMultipleIds completed: Loaded {} tags for {}/{} records in {}ms", totalTags,
                tagsMap.size(), recordIds.size(), duration);
        return tagsMap;
    }

    @Override
    public Map<String, String> getNamesForMultipleIds(List<String> objectIds, String languageCode) {
        long startTime = System.currentTimeMillis();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ids", objectIds);

        String query = """
                MATCH (obj:XtdObject)-[:NAMES]->(mlt:XtdMultiLanguageText)-[:TEXTS]->(text:XtdText)-[:LANGUAGE]->(lang:XtdLanguage)
                WHERE obj.id IN $ids AND lang.code = $languageCode
                RETURN obj.id AS objectId, text.text AS name
                """;
        parameters.put("languageCode", languageCode);

        Collection<Map<String, Object>> results = neo4jClient.query(query).bindAll(parameters).fetch().all();

        // Simple mapping - one name per object
        Map<String, String> namesMap = new HashMap<>();
        for (Map<String, Object> row : results) {
            String objectId = (String) row.get("objectId");
            String name = (String) row.get("name");
            if (objectId != null && name != null) {
                namesMap.putIfAbsent(objectId, name); // putIfAbsent in case of duplicates
            }
        }

        // Fallback to English for objects without names in the requested language
        if (!languageCode.equals("en")) {
            List<String> missingIds = objectIds.stream()
                    .filter(id -> !namesMap.containsKey(id))
                    .collect(Collectors.toList());

            if (!missingIds.isEmpty()) {
                log.debug("Attempting fallback to English for {} objects without names in language: {}", 
                          missingIds.size(), languageCode);

                String fallbackQuery = """
                        MATCH (obj:XtdObject)-[:NAMES]->(mlt:XtdMultiLanguageText)-[:TEXTS]->(text:XtdText)-[:LANGUAGE]->(lang:XtdLanguage)
                        WHERE obj.id IN $ids AND lang.code = 'en'
                        RETURN obj.id AS objectId, text.text AS name
                        """;

                Collection<Map<String, Object>> fallbackResults = neo4jClient.query(fallbackQuery)
                        .bind(missingIds).to("ids").fetch().all();

                for (Map<String, Object> row : fallbackResults) {
                    String objectId = (String) row.get("objectId");
                    String name = (String) row.get("name");
                    if (objectId != null && name != null) {
                        namesMap.putIfAbsent(objectId, name);
                    }
                }

                log.debug("Fallback completed: Found {} additional English names", 
                          fallbackResults.size());
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        log.debug("getNamesForMultipleIds completed: Loaded {} names for {}/{} objects in {}ms (language: {}, with fallback)", 
                  namesMap.size(), namesMap.size(), objectIds.size(), duration, languageCode);
        return namesMap;
    }
}
