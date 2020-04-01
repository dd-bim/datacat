package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CountAllGroupedByQuery<T, ID extends Serializable>
		extends AbstractCustomQuery<T>
		implements CountQuery<T> {

	private static final String QUERY =
			"MATCH (root:${label})<-[:GROUPS]-(relatingThing) " +
			"WHERE relatingThing.id = {relatingThingId} " +
			"RETURN COUNT(root)";

	public CountAllGroupedByQuery(Class<T> entityType, Session session, String relatingThingId) {
		super(entityType, session);

		this.queryParameters.put("relatingThingId", relatingThingId);
	}

	@Override
	public @NotNull String getQueryTemplate() {
		return QUERY;
	}

	@Override
	public long execute() {
		return session.queryForObject(Long.class, this.prepareCypherQuery(), this.queryParameters);
	}
}
