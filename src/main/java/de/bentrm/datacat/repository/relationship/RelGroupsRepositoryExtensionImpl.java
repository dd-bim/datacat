package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.impl.RootRepositoryExtensionImpl;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelGroupsRepositoryExtensionImpl extends RootRepositoryExtensionImpl<XtdRelGroups> implements RelGroupsRepositoryExtension {

    Logger logger = LoggerFactory.getLogger(RelGroupsRepositoryExtensionImpl.class);

    private static final String FIND_BY_RELATING_OBJECT_ID_QUERY_TEMPLATE =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root:${label})<-[:GROUPS]-(relatingObject) " +
            "WHERE relatingObject.id = {relatingObjectId} " +
            "WITH root, name ORDER BY ${orderByClause} " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String COUNT_FIND_BY_RELATING_OBJECT_ID_QUERY_TEMPLATE =
            "MATCH (root:${label})<-[:GROUPS]-(relatingObject) " +
            "WHERE relatingObject.id = {relatingObjectId} " +
            "RETURN count(root)";

    private static final String FIND_BY_RELATED_OBJECT_ID_QUERY_TEMPLATE =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root:${label})-[:GROUPS]->(relatedObject) " +
                    "WHERE relatedObject.id = {relatedObjectId} " +
                    "WITH root, name ORDER BY ${orderByClause} " +
                    "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
                    "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String COUNT_FIND_BY_RELATED_OBJECT_ID_QUERY_TEMPLATE =
            "MATCH (root:${label})<-[:GROUPS]-(relatedObject) " +
                    "WHERE relatedObject.id = {relatedObjectId} " +
                    "RETURN count(root)";

    protected RelGroupsRepositoryExtensionImpl() {
        super(XtdRelGroups.class, XtdRelGroups.LABEL);
    }

    @Override
    public Page<XtdRelGroups> findByRelatingObjectId(String relatingObjectId, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            pageable = getDefaultPageable();
        }

        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("relatingObjectId", relatingObjectId),
                Map.entry("skip", pageable.getOffset()),
                Map.entry("limit", pageable.getPageSize())
        );

        String query = prepareFindByRelatingObjectIdQuery(getOrderByClause(pageable));

        Iterable<XtdRelGroups> nodes = session.query(entityType, query, parameters);
        List<XtdRelGroups> content = new ArrayList<>();
        nodes.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable,
                () -> session.queryForObject(Long.class, prepareCountFindByRelatingObjectIdQuery(), parameters));
    }

    protected String prepareFindByRelatingObjectIdQuery(String orderByClause) {
        Map<String, Object> querySubstitutions = getDefaultSubstitutionParameters();
        querySubstitutions.put("orderByClause", orderByClause);
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(FIND_BY_RELATING_OBJECT_ID_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected String prepareCountFindByRelatingObjectIdQuery() {
        Map<String, Object> querySubstitutions = getDefaultSubstitutionParameters();
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(COUNT_FIND_BY_RELATING_OBJECT_ID_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    @Override
    public Page<XtdRelGroups> findByRelatedObjectId(String relatedObjectId, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            pageable = getDefaultPageable();
        }

        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("relatedObjectId", relatedObjectId),
                Map.entry("skip", pageable.getOffset()),
                Map.entry("limit", pageable.getPageSize())
        );

        String query = prepareFindByRelatedObjectIdQuery(getOrderByClause(pageable));

        Iterable<XtdRelGroups> nodes = session.query(entityType, query, parameters);
        List<XtdRelGroups> content = new ArrayList<>();
        nodes.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable,
                () -> session.queryForObject(Long.class, prepareCountFindByRelatedObjectIdQuery(), parameters));
    }

    protected String prepareFindByRelatedObjectIdQuery(String orderByClause) {
        Map<String, Object> querySubstitutions = getDefaultSubstitutionParameters();
        querySubstitutions.put("orderByClause", orderByClause);
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(FIND_BY_RELATED_OBJECT_ID_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

    protected String prepareCountFindByRelatedObjectIdQuery() {
        Map<String, Object> querySubstitutions = getDefaultSubstitutionParameters();
        StringSubstitutor substitutor = new StringSubstitutor(querySubstitutions);
        String query = substitutor.replace(COUNT_FIND_BY_RELATED_OBJECT_ID_QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        return query;
    }

}
