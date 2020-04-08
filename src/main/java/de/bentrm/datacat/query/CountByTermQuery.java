package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CountByTermQuery<T, ID extends Serializable> extends AbstractCustomQuery<T> implements CountQuery<T> {

    private static final String COUNT_FIND_BY_TERM_QUERY_TEMPLATE =
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node AS hit, score " +
            "MATCH (hit)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(root:${label}) " +
            "RETURN count(DISTINCT root)";

    public CountByTermQuery(Class<T> entityType, Session session, String term) {
        super(entityType, session);
        this.queryParameters.put("term", term);
    }

    @Override
    public long execute() {
        return session.queryForObject(Long.class, this.prepareCypherQuery(), this.queryParameters);
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return COUNT_FIND_BY_TERM_QUERY_TEMPLATE;
    }
}
