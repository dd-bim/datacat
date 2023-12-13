package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component("xtdRelationshipResolver")
public class XtdRelationshipResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "XtdRelationship";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRelationship obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdRelAssignsCollections) {
            return schema.getObjectType(XtdRelAssignsCollections.LABEL);
        }
        if (obj instanceof XtdRelAssignsMeasures) {
            return schema.getObjectType(XtdRelAssignsMeasures.LABEL);
        }
        if (obj instanceof XtdRelAssignsProperties) {
            return schema.getObjectType(XtdRelAssignsProperties.LABEL);
        }
        if (obj instanceof XtdRelAssignsUnits) {
            return schema.getObjectType(XtdRelAssignsUnits.LABEL);
        }
        if (obj instanceof XtdRelAssignsValues) {
            return schema.getObjectType(XtdRelAssignsValues.LABEL);
        }
        if (obj instanceof XtdRelClassifies) {
            return schema.getObjectType(XtdRelClassifies.LABEL);
        }
        if (obj instanceof XtdRelCollects) {
            return schema.getObjectType(XtdRelCollects.LABEL);
        }
        // if (obj instanceof XtdRelDocuments) {
        //     return schema.getObjectType(XtdRelDocuments.LABEL);
        // }

        throw new NotImplementedException("Unsupported relationships type: " + obj);
    }
}
