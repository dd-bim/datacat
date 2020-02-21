package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.domain.relationship.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdRelationshipTypeResolver implements TypeResolver {

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();

        if (obj instanceof XtdRelActsUpon) {
            return env.getSchema().getObjectType(XtdRelActsUpon.LABEL);
        }
        if (obj instanceof XtdRelAssignsCollections) {
            return env.getSchema().getObjectType(XtdRelAssignsCollections.LABEL);
        }
        if (obj instanceof XtdRelAssignsMeasures) {
            return env.getSchema().getObjectType(XtdRelAssignsMeasures.LABEL);
        }
        if (obj instanceof XtdRelAssignsProperties) {
            return env.getSchema().getObjectType(XtdRelAssignsProperties.LABEL);
        }
        if (obj instanceof XtdRelAssignsPropertyWithValues) {
            return env.getSchema().getObjectType(XtdRelAssignsPropertyWithValues.LABEL);
        }
        if (obj instanceof XtdRelAssignsUnit) {
            return env.getSchema().getObjectType(XtdRelAssignsUnit.LABEL);
        }
        if (obj instanceof XtdRelAssignsValues) {
            return env.getSchema().getObjectType(XtdRelAssignsValues.LABEL);
        }
        if (obj instanceof XtdRelClassifies) {
            return env.getSchema().getObjectType(XtdRelClassifies.LABEL);
        }
        if (obj instanceof XtdRelCollects) {
            return env.getSchema().getObjectType(XtdRelCollects.LABEL);
        }
        if (obj instanceof XtdRelComposes) {
            return env.getSchema().getObjectType(XtdRelComposes.LABEL);
        }
        if (obj instanceof XtdRelDocuments) {
            return env.getSchema().getObjectType(XtdRelDocuments.LABEL);
        }
        if (obj instanceof XtdRelGroups) {
            return env.getSchema().getObjectType(XtdRelGroups.LABEL);
        }
        if (obj instanceof XtdRelSequences) {
            return env.getSchema().getObjectType(XtdRelSequences.LABEL);
        }
        if (obj instanceof XtdRelSpecializes) {
            return env.getSchema().getObjectType(XtdRelSpecializes.LABEL);
        }

        throw new NotImplementedException("Unsupported relationships type: " + obj);
    }
}
