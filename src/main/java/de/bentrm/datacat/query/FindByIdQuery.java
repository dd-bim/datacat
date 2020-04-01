package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

public class FindByIdQuery<T, ID extends Serializable> extends AbstractCustomQuery<T> implements CustomQuery, EntityQuery<T> {

	private static final String FIND_BY_ID_QUERY_TEMPLATE =
			"MATCH (root:${label} {id: {id}}) RETURN root, ${propertyAggregations}, ID(root)";

	public FindByIdQuery(Class<T> entityType, Session session, ID id) {
		super(entityType, session);
		this.queryParameters.put("id", id);
	}

	@Override
	public @NotNull String getQueryTemplate() {
		return FIND_BY_ID_QUERY_TEMPLATE;
	}

	@Override
	public Optional<T> execute() {
		T result = this.session.queryForObject(this.entityType, this.prepareCypherQuery(), this.queryParameters);
		return Optional.ofNullable(result);
	}
}
