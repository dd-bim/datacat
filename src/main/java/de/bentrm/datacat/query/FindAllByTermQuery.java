package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public class FindAllByTermQuery<T> extends AbstractCustomQuery<T> implements IterableQuery<T> {

    private static final String FIND_BY_TERM_QUERY_TEMPLATE =
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node AS hit, score " +
            "MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root:${label}) " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    public FindAllByTermQuery(Class<T> entityType, Session session, String term, Pageable pageable) {
        super(entityType, session);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, pageable.getSortOr(DEFAULT_SORT_ORDER));
        }

        this.queryParameters.put("term", term);
        this.queryParameters.put("skip", pageable.getOffset());
        this.queryParameters.put("limit", pageable.getPageSize());
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return FIND_BY_TERM_QUERY_TEMPLATE;
    }

    @Override
    public Iterable<T> execute() {
        return this.session.query(this.entityType, this.prepareCypherQuery(), this.queryParameters);
    }
}
