package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class CountAllQuery<T> extends AbstractCustomQuery<T> implements CountQuery<T> {

    private final String QUERY = """
            MATCH (root)
            WHERE
                size([label IN labels(root) WHERE label IN $labels | 1]) > 0
                AND size([label IN labels(root) WHERE label IN $excludedLabels | 1]) = 0
                AND NOT root.id IN $excludedIds
            RETURN COUNT(root)
            """;

    public CountAllQuery(Class<T> entityType, Session session, FilterOptions filterOptions) {
        super(entityType, session);

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
    }

    @Override
    public long execute() {
        return this.session.queryForObject(Long.class, this.prepareCypherQuery(), this.queryParameters);
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return QUERY;
    }
}
