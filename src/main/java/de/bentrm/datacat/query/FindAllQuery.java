package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class FindAllQuery<T> extends AbstractCustomQuery<T> implements IterableQuery<T> {

    private static final String FIND_ALL_QUERY_TEMPLATE =
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(root) " +
            "WHERE " +
                    "size([label IN labels(root) WHERE label IN {labels} | 1]) > 0 " +
                    "AND size([label IN labels(root) WHERE label IN {excludedLabels} | 1]) = 0 " +
                    "AND NOT root.id IN {excludedIds} " +
            "WITH root, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT root SKIP {skip} LIMIT {limit} " +
            "RETURN root, ${propertyAggregations}, ID(root)";

    public FindAllQuery(Class<T> entityType, Session session, @NotNull Pageable pageable) {
        this(entityType, session, pageable, new FilterOptions(null, null, null));
    }

    public FindAllQuery(Class<T> entityType, Session session, @NotNull Pageable pageable, FilterOptions filterOptions) {
        super(entityType, session);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, pageable.getSortOr(DEFAULT_SORT_ORDER));
        }

        final Set<String> labels = filterOptions.getLabels();
        labels.add(getNodeEntityLabel());

        for (String label : labels) {
            if (filterOptions.getExcludedLabels().contains(label)) {
                throw new IllegalStateException(String.format("Requested label '%s' is excluded from result set.", label));
            }
        }

        this.queryParameters.put("labels", labels);
        this.queryParameters.put("excludedLabels", filterOptions.getExcludedLabels());
        this.queryParameters.put("excludedIds", filterOptions.getExcludedIds());
        this.queryParameters.put("skip", pageable.getOffset());
        this.queryParameters.put("limit", pageable.getPageSize());
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return FIND_ALL_QUERY_TEMPLATE;
    }

    @Override
    public Iterable<T> execute() {
        return this.session.query(this.entityType, this.prepareCypherQuery(), this.queryParameters);
    }
}
