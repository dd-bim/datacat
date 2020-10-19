package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdRelationshipResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "XtdRelationship";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRelationship obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdRelActsUpon) {
            return schema.getObjectType(XtdRelActsUpon.LABEL);
        }
        if (obj instanceof XtdRelAssignsCollections) {
            return schema.getObjectType(XtdRelAssignsCollections.LABEL);
        }
        if (obj instanceof XtdRelAssignsMeasures) {
            return schema.getObjectType(XtdRelAssignsMeasures.LABEL);
        }
        if (obj instanceof XtdRelAssignsProperties) {
            return schema.getObjectType(XtdRelAssignsProperties.LABEL);
        }
        if (obj instanceof XtdRelAssignsPropertyWithValues) {
            return schema.getObjectType(XtdRelAssignsPropertyWithValues.LABEL);
        }
        if (obj instanceof XtdRelAssignsUnit) {
            return schema.getObjectType(XtdRelAssignsUnit.LABEL);
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
        if (obj instanceof XtdRelAssociates) {
            return schema.getObjectType(XtdRelAssociates.LABEL);
        }
        if (obj instanceof XtdRelComposes) {
            return schema.getObjectType(XtdRelComposes.LABEL);
        }
        if (obj instanceof XtdRelDocuments) {
            return schema.getObjectType(XtdRelDocuments.LABEL);
        }
        if (obj instanceof XtdRelGroups) {
            return schema.getObjectType(XtdRelGroups.LABEL);
        }
        if (obj instanceof XtdRelSequences) {
            return schema.getObjectType(XtdRelSequences.LABEL);
        }
        if (obj instanceof XtdRelSpecializes) {
            return schema.getObjectType(XtdRelSpecializes.LABEL);
        }

        throw new NotImplementedException("Unsupported relationships type: " + obj);
    }
}
