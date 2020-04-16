package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class FindAllByIdQuery<T, ID extends Serializable> extends AbstractCustomQuery<T> implements IterableQuery<T> {

    private static final String FIND_BY_IDS_QUERY_TEMPLATE =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root:${label}) " +
            "WHERE root.id IN {ids} " +
            "WITH root, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    public FindAllByIdQuery(Class<T> entityType, Session session, Iterable<ID> ids, Pageable pageable) {
        super(entityType, session);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, pageable.getSortOr(DEFAULT_SORT_ORDER));
        }

        this.queryParameters.put("ids", ids);
        this.queryParameters.put("skip", pageable.getOffset());
        this.queryParameters.put("limit", pageable.getPageSize());
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return FIND_BY_IDS_QUERY_TEMPLATE;
    }

    @Override
    public Iterable<T> execute() {
        return this.session.query(this.entityType, this.prepareCypherQuery(), this.queryParameters);
    }
}