package de.bentrm.datacat.query;

import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CountAllGroupingQuery<T, ID extends Serializable>
		extends AbstractCustomQuery<T>
		implements CountQuery<T> {

	private static final String QUERY =
			"MATCH (root:${label})<-[:GROUPS]-(relatedThing) " +
			"WHERE relatedThing.id = {relatedThingId} " +
			"RETURN COUNT(root)";

	public CountAllGroupingQuery(Class<T> entityType, Session session, String relatedThingId) {
		super(entityType, session);
		this.queryParameters.put("relatedThingId", relatedThingId);
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
