package de.bentrm.datacat.graphql.type.resolver;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class XtdRootTypeResolver implements TypeResolver {

    @Autowired
    private XtdObjectTypeResolver objectTypeResolver;

    @Autowired
    private XtdCollectionTypeResolver collectionTypeResolver;

    @Autowired
    private XtdRelationshipTypeResolver relationshipTypeResolver;

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRoot obj = env.getObject();

        if (obj instanceof XtdObject) {
            return objectTypeResolver.getType(env);
        }
        if (obj instanceof XtdCollection) {
            return collectionTypeResolver.getType(env);
        }
        if (obj instanceof XtdRelationship) {
            return relationshipTypeResolver.getType(env);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
