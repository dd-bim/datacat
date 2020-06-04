package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.User;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityTypeResolver implements TypeResolver {

    @Autowired
    private CatalogItemTypeResolver entityTypeResolver;

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
