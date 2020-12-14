package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdObjectResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "XtdObject";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdActivity) {
            return schema.getObjectType(XtdActivity.LABEL);
        }
        if (obj instanceof XtdActor) {
            return schema.getObjectType(XtdActor.LABEL);
        }
        if (obj instanceof XtdClassification) {
            return schema.getObjectType(XtdClassification.LABEL);
        }
        if (obj instanceof Measure) {
            return schema.getObjectType(Measure.LABEL);
        }
        if (obj instanceof XtdProperty) {
            return schema.getObjectType(XtdProperty.LABEL);
        }
        if (obj instanceof XtdSubject) {
            return schema.getObjectType(XtdSubject.LABEL);
        }
        if (obj instanceof XtdUnit) {
            return schema.getObjectType(XtdUnit.LABEL);
        }
        if (obj instanceof XtdValue) {
            return schema.getObjectType(XtdValue.LABEL);
        }

        throw new NotImplementedException("Unsupported type: " + obj);
    }
}
