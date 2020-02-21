package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdRootTypeResolver implements TypeResolver {

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRoot obj = env.getObject();

        if (obj instanceof XtdObject) {
            return env.getSchema().getObjectType(XtdObject.LABEL);
        }
        if (obj instanceof XtdCollection) {
            return env.getSchema().getObjectType(XtdCollection.LABEL);
        }
        if (obj instanceof XtdRelationship) {
            return env.getSchema().getObjectType(XtdRelationship.LABEL);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
