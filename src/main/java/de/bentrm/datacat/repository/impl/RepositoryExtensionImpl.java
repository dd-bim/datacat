package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.repository.RepositoryExtension;
import de.bentrm.datacat.repository.object.impl.SubjectRepositoryExtensionImpl;
import org.apache.commons.text.StringSubstitutor;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.*;

public abstract class RepositoryExtensionImpl<T extends Entity> implements RepositoryExtension<T> {

    Logger logger = LoggerFactory.getLogger(SubjectRepositoryExtensionImpl.class);

    public final int DEFAULT_PAGE_NUMBER = 1;
    public final int DEFAULT_PAGE_SIZE = 10;

    private static final String FIND_BY_ID_QUERY_TEMPLATE =
            "MATCH (root:${label} {id: {id}}) " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String FIND_BY_IDS_QUERY_TEMPLATE =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root:${label}) " +
            "WHERE root.id IN {ids} " +
            "WITH root, name ORDER BY ${orderByClause} " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String COUNT_FIND_BY_IDS_QUERY_TEMPLATE =
            "MATCH (root:${label}) WHERE root.id IN {ids} " +
            "RETURN count(root)";

    private static final String FIND_ALL_QUERY_TEMPLATE =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root:${label}) " +
            "WITH root, name ORDER BY ${orderByClause} " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String FIND_BY_TERM_QUERY_TEMPLATE =
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node AS hit, score " +
            "MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root:${label}) " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String COUNT_FIND_BY_TERM_QUERY_TEMPLATE =
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node AS hit, score " +
            "MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root:${label}) " +
            "RETURN count(DISTINCT root)";

    private final String ORDER_BY_NAME_ASC = "name.sortOrder, toLower(name.name) ASC, name.name DESC";
    private final String ORDER_BY_NAME_DESC = "name.sortOrder, toLower(name.name) DESC, name.name DESC";

    @Autowired
    protected Session session;

    protected Class<T> entityType;
    protected String label;

    protected RepositoryExtensionImpl(Class<T> entityType, String label) {
        this.entityType = entityType;
        this.label = label;
    }

    @Override
    public Optional<T> findByUID(String uid) {
        Map<String, String> parameters = Map.of("id", uid);
        T result = session.queryForObject(entityType, prepareFindByUIDQuery(), parameters);
        return Optional.ofNullable(result);
    }

    @Override
    public Page<T> findByUIDs(List<String> uids, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            pageable = getDefaultPageable();
        }

        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("ids", uids),
                Map.entry("skip", pageable.getOffset()),
                Map.entry("limit", pageable.getPageSize())
        );

        String query = prepareFindByUIDsQuery(getOrderByClause(pageable));

        Iterable<T> nodes = session.query(entityType, query, parameters);
        List<T> content = new ArrayList<>();
        nodes.forEach(content::add);
        return PageableExecutionUtils.getPage(content,
                pageable, () -> session.queryForObject(Long.class, prepareCountFindByUIDsQuery(), parameters));
    }

    protected String prepareFindByUIDQuery() {
        StringSubstitutor substitutor = new StringSubstitutor(getDefaultSubstitutionParameters());
        String query = substitutor.replace(FIND_BY_ID_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected String prepareFindByUIDsQuery(String orderByClause) {
        Map<String, Object> querySubstitutions = getDefaultSubstitutionParameters();
        querySubstitutions.put("orderByClause", orderByClause);
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(FIND_BY_IDS_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected String prepareCountFindByUIDsQuery() {
        Map<String, ?> values = Map.ofEntries(
                Map.entry("label", this.label)
        );
        StringSubstitutor substitutor = new StringSubstitutor(values);
        String query = substitutor.replace(COUNT_FIND_BY_IDS_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected Map<String, Object> getDefaultSubstitutionParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("label", this.label);
        parameters.put("propertyAggregations", getEntityPropertyAggregations());
        return parameters;
    }

    @Override
    public Page<T> findAll() {
        return findAll(getDefaultPageable());
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
                pageable = getDefaultPageable();
        }

        logger.debug("Using session: {}", session);
        logger.debug("Current transaction: {}", session.getTransaction());

        String query = prepareFindAllQuery(getOrderByClause(pageable));
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("skip", pageable.getOffset()),
                Map.entry("limit", pageable.getPageSize())
        );

        Iterable<T> nodes = session.query(entityType, query, parameters);
        List<T> content = new ArrayList<>();
        nodes.forEach(content::add);
        return PageableExecutionUtils.getPage(content,
                pageable, () -> session.countEntitiesOfType(entityType));
    }

    protected String prepareFindAllQuery(String orderByClause) {
        Map<String, Object> querySubstitutions = getDefaultSubstitutionParameters();
        querySubstitutions.put("orderByClause", orderByClause);
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(FIND_ALL_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    @Override
    public Page<T> findByTerm(String term) {
        logger.debug("Unpaged 'findByTerm' query...");
        return findAll(getDefaultPageable());
    }

    @Override
    public Page<T> findByTerm(String term, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            pageable = getDefaultPageable(Sort.by(Sort.Order.desc("score")));
        }

        logger.debug("Using session: {}", session);
        logger.debug("Current transaction: {}", session.getTransaction());

        String query = prepareFindByTermQuery();
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("term", term),
                Map.entry("skip", pageable.getOffset()),
                Map.entry("limit", pageable.getPageSize())
        );
        Iterable<T> nodes = session.query(entityType, query, parameters);
        List<T> content = new ArrayList<>();
        nodes.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable,
                () -> session.queryForObject(Long.class, prepareCountFindByTermQuery(), parameters));
    }

    protected String prepareFindByTermQuery() {
        Map<String, ?> querySubstitutions = Map.ofEntries(
                Map.entry("label", this.label),
                Map.entry("propertyAggregations", getEntityPropertyAggregations())
        );
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(FIND_BY_TERM_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected String prepareCountFindByTermQuery() {
        Map<String, ?> values = Map.ofEntries(
                Map.entry("label", this.label)
        );
        StringSubstitutor substitutor = new StringSubstitutor(values);
        String query = substitutor.replace(COUNT_FIND_BY_TERM_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected String getOrderByClause(Pageable pageable) {
        String orderByClause = ORDER_BY_NAME_ASC;
        Sort sort = pageable.getSortOr(getDefaultSort());
        for (Sort.Order order : sort) {
            if (order.getProperty().equals("name")) {
                if (order.isAscending()) {
                    orderByClause = ORDER_BY_NAME_ASC;
                } else {
                    orderByClause = ORDER_BY_NAME_DESC;
                }
            }
        }
        return orderByClause;
    }

    protected String getEntityPropertyAggregations() {
        return "[ p=(root)<-[:IS_NAME_OF|IS_DESCRIPTION_OF|COMMENTS*0..1]-() | [relationships(p), nodes(p)] ]";
    }

    protected Pageable getDefaultPageable() {
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, getDefaultSort());
    }

    protected Pageable getDefaultPageable(Sort sort) {
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, sort);
    }

    protected Sort getDefaultSort() {
        return Sort.by(Sort.Order.asc("name"));
    }

}
