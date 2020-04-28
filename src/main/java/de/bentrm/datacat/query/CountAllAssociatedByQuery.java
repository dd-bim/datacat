package de.bentrm.datacat.query;

import de.bentrm.datacat.domain.relationship.Association;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.session.Session;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public class CountAllAssociatedByQuery<T extends Association, ID extends Serializable>
        extends AbstractCustomQuery<T>
        implements CountQuery<T> {

    private static final String QUERY =
            "MATCH (root:${label})<-[:${associationLabel}]-(relatingThing) " +
            "WHERE relatingThing.id = {relatingThingId} " +
            "RETURN COUNT(root)";

    public CountAllAssociatedByQuery(Class<T> entityType, Session session, String relatingThingId) {
        super(entityType, session);

        this.queryParameters.put("relatingThingId", relatingThingId);
    }

    @Override
    public @NotNull String getQueryTemplate() {
        return QUERY;
    }

    @Override
    public @NotNull Map<String, Object> getSubstitutionParameters() {
        @NotNull final Map<String, Object> substitutionParameters = super.getSubstitutionParameters();

        try {
            final Field field = this.entityType.getDeclaredField("relatingThing");
            final Relationship annotation = field.getAnnotation(Relationship.class);
            substitutionParameters.put("associationLabel", annotation.type());
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Invalid access to association relationship type");
        }

        return substitutionParameters;
    }

    @Override
    public long execute() {
        return session.queryForObject(Long.class, this.prepareCypherQuery(), this.queryParameters);
    }
}
