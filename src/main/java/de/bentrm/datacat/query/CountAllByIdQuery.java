package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;

public class CountAllByIdQuery<T> extends AbstractCustomQuery<T> implements CountQuery<T> {

    private static final String COUNT_FIND_ALL_BY_ID_QUERY_TEMPLATE = """
        MATCH (root:${label}) WHERE root.id IN $ids RETURN COUNT(root)
        """;

    public CountAllByIdQuery(Class<T> entityType, Session session, Iterable<String> ids) {
        super(entityType, session);
        this.queryParameters.put("ids", ids);
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return COUNT_FIND_ALL_BY_ID_QUERY_TEMPLATE;
    }

    @Override
    public long execute() {
        return session.queryForObject(Long.class, this.prepareCypherQuery(), this.queryParameters);
    }
}
