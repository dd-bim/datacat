package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

public class SearchQuery<T, ID extends Serializable> extends AbstractCustomQuery<T> implements IterableQuery<T> {

    private static final String QUERY =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root) " +
            "WHERE " +
                "size([label IN labels(root) WHERE label IN {labels} | 1]) > 0 " +
                "AND size([label IN labels(root) WHERE label IN {excludedLabels} | 1]) = 0 " +
                "AND NOT root.id IN {excludedIds} " +
            "WITH root, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    private static final String FULL_TEXT_SEARCH_QUERY =
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node AS hit, score " +
            "MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root) " +
            "WHERE " +
                "size([label IN labels(root) WHERE label IN {labels} | 1]) > 0 " +
                "AND size([label IN labels(root) WHERE label IN {excludedLabels} | 1]) = 0 " +
                "AND NOT root.id IN {excludedIds} " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    public SearchQuery(Class<T> entityType, Session session, @NotNull SearchOptions<ID> options, @NotNull Pageable pageable) {
        super(entityType, session);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, pageable.getSortOr(DEFAULT_SORT_ORDER));
        }

        for (String label : options.getLabels()) {
            if (options.getExcludedLabels().contains(label)) {
                throw new IllegalStateException(String.format("Requested label '%s' is excluded from result set.", label));
            }
        }

        if (options.getTerm() != null && !options.getTerm().isBlank()) {
            this.queryParameters.put("term", options.getTerm());
        }

        if (!options.getLabels().isEmpty()) {
            this.queryParameters.put("labels", options.getLabels());
        } else {
            this.queryParameters.put("labels", Set.of("XtdEntity"));
        }

        this.queryParameters.put("excludedLabels", options.getExcludedLabels());
        this.queryParameters.put("excludedIds", options.getExcludedIds());
        this.queryParameters.put("skip", pageable.getOffset());
        this.queryParameters.put("limit", pageable.getPageSize());
    }

    @Override
    public @NotNull String getQueryTemplate() {
        if (this.queryParameters.containsKey("term")) {
            return FULL_TEXT_SEARCH_QUERY;
        }
        return QUERY;
    }

    @Override
    public Iterable<T> execute() {
        return this.session.query(this.entityType, this.prepareCypherQuery(), this.queryParameters);
    }
}
