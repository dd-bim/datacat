package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityResolver implements CustomResolver {

    @Autowired
    private ConceptResolver entityTypeResolver;

    @Override
    public String getTypeName() {
        return "Entity";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();

        if (obj instanceof User) {
            return env.getSchema().getObjectType("User");
        }

        if (obj instanceof CatalogItem) {
            return entityTypeResolver.getType(env);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
