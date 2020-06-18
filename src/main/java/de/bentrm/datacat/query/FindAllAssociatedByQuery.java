package de.bentrm.datacat.query;

import de.bentrm.datacat.domain.relationship.Association;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.session.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Map;

public class FindAllAssociatedByQuery<T extends Association>
        extends AbstractCustomQuery<T>
        implements IterableQuery<T> {

    private static final String QUERY = """
            MATCH (name:XtdName)-[:IS_NAME_OF]->(root:${label})<-[:${associationLabel}]-(relatingThing)
            WHERE relatingThing.id = $relatingThingId
            WITH root, name ORDER BY name.sortOrder, toLower(name.value) ASC, name.value DESC
            WITH DISTINCT root SKIP $skip LIMIT $limit
            RETURN root, ${propertyAggregations}, ID(root)
            """;

    public FindAllAssociatedByQuery(Class<T> entityType, Session session, String relatingThingId, Pageable pageable) {
        super(entityType, session);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, pageable.getSortOr(DEFAULT_SORT_ORDER));
        }

        this.queryParameters.put("relatingThingId", relatingThingId);
        this.queryParameters.put("skip", pageable.getOffset());
        this.queryParameters.put("limit", pageable.getPageSize());
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
    public Iterable<T> execute() {
        return session.query(this.entityType, this.prepareCypherQuery(), this.queryParameters);
    }
}
