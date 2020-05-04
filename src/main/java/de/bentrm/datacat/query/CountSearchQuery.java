package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

public class CountSearchQuery<T, ID extends Serializable> extends AbstractCustomQuery<T> implements CountQuery<T> {

    private final String QUERY =
            "MATCH (root) " +
            "WHERE " +
                "size([label IN labels(root) WHERE label IN {labels} | 1]) > 0 " +
                "AND size([label IN labels(root) WHERE label IN {excludedLabels} | 1]) = 0 " +
                "AND NOT root.id IN {excludedIds} " +
            "RETURN COUNT(root)";

    private final String FULL_TEXT_SEARCH_QUERY =
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node AS hit, score " +
            "MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root) " +
            "WHERE " +
                "size([label IN labels(root) WHERE label IN {labels} | 1]) > 0 " +
                "AND size([label IN labels(root) WHERE label IN {excludedLabels} | 1]) = 0 " +
                "AND NOT root.id IN {excludedIds} " +
            "RETURN count(DISTINCT root)";

    public CountSearchQuery(Class<T> entityType, Session session, FilterOptions<ID> options) {
        super(entityType, session);

        final Set<String> labels = options.getLabels();
        labels.add(getNodeEntityLabel());

        for (String label : labels) {
            if (options.getExcludedLabels().contains(label)) {
                throw new IllegalStateException(String.format("Requested label '%s' is excluded from result set.", label));
            }
        }

        if (options.getTerm() != null && !options.getTerm().isBlank()) {
            this.queryParameters.put("term", options.getTerm());
        }
        this.queryParameters.put("labels", labels);
        this.queryParameters.put("excludedLabels", options.getExcludedLabels());
        this.queryParameters.put("excludedIds", options.getExcludedIds());
    }

    @Override
    public long execute() {
        return this.session.queryForObject(Long.class, this.prepareCypherQuery(), this.queryParameters);
    }

    @Override
    public @NotNull String getQueryTemplate() {
        if (this.queryParameters.containsKey("term")) {
            return FULL_TEXT_SEARCH_QUERY;
        }
        return QUERY;
    }
}
