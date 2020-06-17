package de.bentrm.datacat.query;

import de.bentrm.datacat.domain.PropertyQueryHint;
import org.apache.commons.text.StringSubstitutor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ListQuery<T> {

    private static final Logger logger = LoggerFactory.getLogger(ListQuery.class);

    private static final String QUERY_TEMPLATE = """
            ${matchClause}
            WHERE ${whereClauses}
            WITH DISTINCT root SKIP {skip} LIMIT {limit}
            RETURN root, ${propertyAggregations}, ID(root)
            """;

    private static final String FULL_TEXT_MATCH = """
            CALL db.index.fulltext.queryNodes({index}, {searchTerm}) YIELD node AS hit, score
            MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root)""";

    private static final String MATCH = """
            MATCH (name:XtdName)-[:IS_NAME_OF]->(root)
            WITH root, name ORDER BY name.sortOrder, toLower(name.value) ASC, name.value DESC""";

    protected final Class<T> entityType;
    protected final Session session;
    protected final List<String> whereClauses = new ArrayList<>();
    protected final Map<String, Object> queryParameters = new HashMap<>();
    protected boolean isFullTextSearch = false;

    private ListQuery(Class<T> entityType, Session session) {
        this.entityType = entityType;
        this.session = session;
    }

    public Iterable<T> execute() {
        StringSubstitutor substitutor = new StringSubstitutor(this.getSubstitutionParameters());
        String query = substitutor.replace(QUERY_TEMPLATE);
        logger.debug("Prepared query statement: {}", query);
        logger.debug("Parameters: {}", queryParameters);
        return this.session.query(this.entityType, query, this.queryParameters);
    }

    protected Map<String, Object> getSubstitutionParameters() {
        return Map.ofEntries(
            Map.entry("matchClause", this.getMatchClauseSubstitution()),
            Map.entry("whereClauses", this.getWhereClauseSubstitution()),
            Map.entry("propertyAggregations", this.getPropertyAggregationSubstitution())
        );
    }

    private String getMatchClauseSubstitution() {
        return this.isFullTextSearch ? FULL_TEXT_MATCH : MATCH;
    }

    private String getWhereClauseSubstitution() {
        return String.join(" AND ", this.whereClauses);
    }

    private String getPropertyAggregationSubstitution() {
        PropertyQueryHint annotation = entityType.getAnnotation(PropertyQueryHint.class);
        String[] values = annotation.value();

        for (int i = 0; i < values.length; i++) {
            String label = "p" + i;
            values[i] = "[ " + label + "=" + values[i] + " | [relationships(" + label + "), nodes(" + label + ")] ]";
        }

        return String.join(", ", values);
    }

    public static class QueryBuilder<T> {
        protected final Class<T> entityType;
        protected final Session session;

        private String searchTerm;
        private FullTextIndex fullTextIndex = FullTextIndex.ALL;
        private final Set<String> labelsIn = new HashSet<>();
        private final Set<String> labelsNotIn = new HashSet<>();
        private final Set<String> idsIn = new HashSet<>();
        private final Set<String> idsNotIn = new HashSet<>();
        private long skip = 0;
        private int limit = 10;

        public QueryBuilder(Class<T> entityType, Session session) {
            this.entityType = entityType;
            this.session = session;
        }

        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        public void setFullTextIndex(FullTextIndex fullTextIndex) {
            this.fullTextIndex = fullTextIndex;
        }

        public void setLabelsIn(List<String> labelsIn) {
            if (labelsIn != null) {
                this.labelsIn.addAll(labelsIn);
            }
        }

        public void setLabelsNotIn(List<String> labelsNotIn) {
            if (labelsNotIn != null) {
                this.labelsNotIn.addAll(labelsNotIn);
            }
        }

        public void setIdsIn(List<String> idsIn) {
            if (idsIn != null) {
                this.idsIn.addAll(idsIn);
            }
        }

        public void setIdsNotIn(List<String> idsNotIn) {
            if (idsNotIn != null) {
                this.idsNotIn.addAll(idsNotIn);
            }
        }

        public void setSkip(long skip) {
            this.skip = skip;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public ListQuery<T> build() {
            ListQuery<T> query = new ListQuery<>(this.entityType, this.session);

            if (this.searchTerm != null && !searchTerm.isBlank()) {
                query.isFullTextSearch = true;
                query.queryParameters.put("index", this.fullTextIndex.getIndexName());
                query.queryParameters.put("searchTerm", this.searchTerm.trim());
            }

            if (this.labelsIn.isEmpty()) {
                NodeEntity annotation = entityType.getAnnotation(NodeEntity.class);
                this.labelsIn.add(annotation.label());
            }

            query.queryParameters.put("labels", this.labelsIn);
            query.whereClauses.add("size([label IN labels(root) WHERE label IN {labels} | 1]) > 0");

            if (!this.labelsNotIn.isEmpty()) {
                query.queryParameters.put("excludedLabels", this.labelsNotIn);
                query.whereClauses.add("size([label IN labels(root) WHERE label IN {excludedLabels} | 1]) = 0");
            }

            if (!this.idsIn.isEmpty()) {
                query.queryParameters.put("ids", this.idsIn);
                query.whereClauses.add("root.id IN {ids}");
            }

            if (!this.idsNotIn.isEmpty()) {
                query.queryParameters.put("excludedIds", this.idsNotIn);
                query.whereClauses.add("NOT root.id IN {excludedIds}");
            }

            query.queryParameters.put("skip", this.skip);
            query.queryParameters.put("limit", this.limit);

            return query;
        }
    }
}
