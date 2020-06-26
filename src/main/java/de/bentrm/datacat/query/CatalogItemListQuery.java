package de.bentrm.datacat.query;

import de.bentrm.datacat.domain.PropertyQueryHint;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CatalogItemListQuery<T> {

    private static final Logger logger = LoggerFactory.getLogger(CatalogItemListQuery.class);

    protected final Class<T> entityType;
    protected final Session session;
    protected final List<String> whereClauses = new ArrayList<>();
    protected final Map<String, Object> queryParameters = new HashMap<>();
    protected boolean isFullTextSearch = false;

    protected CatalogItemListQuery(Class<T> entityType, Session session) {
        this.entityType = entityType;
        this.session = session;
    }

    public Iterable<T> execute() {
        final String query = this.getQuery();
        logger.debug("Prepared query statement: {}", query);
        logger.debug("Parameters: {}", queryParameters);
        return this.session.query(this.entityType, query, this.queryParameters);
    }

    public int count() {
        final String query = this.getCountQuery();
        logger.debug("Prepared count query statement: {}", query);
        logger.debug("Parameters: {}", queryParameters);
        return this.session.queryForObject(Integer.class, query, this.queryParameters);
    }

    protected String getQuery() {
        final StringBuilder builder = new StringBuilder();
        this.appendMatchClause(builder);
        this.appendWhereClause(builder);
        this.appendWithClause(builder);
        this.appendReturnClause(builder);
        return builder.toString();
    }

    protected String getCountQuery() {
        final StringBuilder builder = new StringBuilder();
        this.appendMatchClause(builder);
        this.appendWhereClause(builder);
        this.appendCountReturnClause(builder);
        return builder.toString();
    }

    protected void appendMatchClause(StringBuilder builder) {
        if (isFullTextSearch) {
            builder.append("""
                CALL db.index.fulltext.queryNodes($index, $searchTerm) YIELD node AS hit, score
                MATCH (hit)<-[:NAMED|:DESCRIBED]-(root)""");
        } else {
            builder.append("""
                MATCH (name:Translation)<-[:NAMED]-(root)
                WITH root, name ORDER BY toLower(name.label) ASC, name.label DESC""");
        }
    }

    protected void appendWhereClause(StringBuilder builder) {
        if (!whereClauses.isEmpty()) {
            builder.append(System.lineSeparator());
            builder.append("WHERE ");

            for (int i = 0; i < whereClauses.size(); i++) {
                builder.append(whereClauses.get(i));
                if (i < whereClauses.size() - 1) {
                    builder.append(" AND ");
                }
            }
        }
    }

    protected void appendWithClause(StringBuilder builder) {
        builder.append(System.lineSeparator());
        builder.append("WITH DISTINCT root SKIP $skip LIMIT $limit");
    }

    protected void appendReturnClause(StringBuilder builder) {
        PropertyQueryHint annotation = entityType.getAnnotation(PropertyQueryHint.class);

        builder.append(System.lineSeparator());
        builder.append("RETURN ID(root), root");

        if (annotation != null) {
            String[] values = annotation.value();
            for (int i = 0; i < values.length; i++) {
                final String format = ", [ p%1$s=%2$s | [relationships(p%1$s), nodes(p%1$s)] ]";
                final String line = String.format(format, i, values[i]);
                builder.append(line);
            }
        }
    }

    protected void appendCountReturnClause(StringBuilder builder) {
        builder.append(System.lineSeparator());
        builder.append("RETURN count(DISTINCT root)");
    }

    public static class Builder<T> {
        protected final Class<T> entityType;
        protected final Session session;

        private String searchTerm;
        private FullTextIndex fullTextIndex = FullTextIndex.ALL;
        private final Set<String> labelsIn = new HashSet<>();
        private List<String> labelsNotIn;
        private List<String> idsIn;
        private List<String> idsNotIn;
        private long skip = 0;
        private int limit = 10;

        public Builder(Class<T> entityType, Session session) {
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
            this.labelsNotIn = labelsNotIn;
        }

        public void setIdsIn(List<String> idsIn) {
            this.idsIn = idsIn;
        }

        public void setIdsNotIn(List<String> idsNotIn) {
            this.idsNotIn = idsNotIn;
        }

        public void setSkip(long skip) {
            this.skip = skip;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public CatalogItemListQuery<T> build() {
            CatalogItemListQuery<T> query = new CatalogItemListQuery<>(this.entityType, this.session);

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
            query.whereClauses.add("size([label IN labels(root) WHERE label IN $labels | 1]) > 0");

            if (this.labelsNotIn != null) {
                query.queryParameters.put("excludedLabels", this.labelsNotIn);
                query.whereClauses.add("size([label IN labels(root) WHERE label IN $excludedLabels | 1]) = 0");
            }

            if (this.idsIn != null) {
                query.queryParameters.put("ids", this.idsIn);
                query.whereClauses.add("root.id IN $ids");
            }

            if (this.idsNotIn != null) {
                query.queryParameters.put("excludedIds", this.idsNotIn);
                query.whereClauses.add("NOT root.id IN $excludedIds");
            }

            query.queryParameters.put("skip", this.skip);
            query.queryParameters.put("limit", this.limit);

            return query;
        }
    }
}
