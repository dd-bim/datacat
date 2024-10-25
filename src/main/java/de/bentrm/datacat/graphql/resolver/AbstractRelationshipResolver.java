package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component("abstractRelationshipResolver")
public class AbstractRelationshipResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "AbstractRelationship";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        AbstractRelationship obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdRelationshipToSubject) {
            return schema.getObjectType(XtdRelationshipToSubject.LABEL);
        }
        if (obj instanceof XtdRelationshipToProperty) {
            return schema.getObjectType(XtdRelationshipToProperty.LABEL);
        }

        throw new NotImplementedException("Unsupported relationships type: " + obj);
    }
}
