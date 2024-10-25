package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XtdObjectResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "XtdObject";
    }

    @Autowired
    private XtdConceptResolver conceptTypeResolver;

    @Autowired AbstractRelationshipResolver abstractRelationshipResolver;

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdObject obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdConcept) {
            return conceptTypeResolver.getType(env);
        }
        if (obj instanceof AbstractRelationship) {
            return abstractRelationshipResolver.getType(env);
        }
        // if (obj instanceof XtdRelationshipToSubject) {
        //     return schema.getObjectType(XtdRelationshipToSubject.LABEL);
        // }
        // if (obj instanceof XtdRelationshipToProperty) {
        //     return schema.getObjectType(XtdRelationshipToProperty.LABEL);
        // }
        if (obj instanceof XtdValue) {
            return schema.getObjectType(XtdValue.LABEL);
        }
        if (obj instanceof XtdOrderedValue) {
            return schema.getObjectType(XtdOrderedValue.LABEL);
        }

        throw new NotImplementedException("Unsupported type: " + obj);
    }
}
