package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XtdRootResolver implements CustomResolver {

    @Autowired
    private XtdObjectResolver objectTypeResolver;

    @Autowired
    private XtdCollectionResolver collectionTypeResolver;

    @Override
    public String getTypeName() {
        return "XtdRoot";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRoot obj = env.getObject();

        if (obj instanceof XtdObject) {
            return objectTypeResolver.getType(env);
        }
        if (obj instanceof XtdCollection) {
            return collectionTypeResolver.getType(env);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
