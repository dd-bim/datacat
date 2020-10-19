package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.XtdRelActsUpon;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class QualifiedOneToOneRelationshipResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "QualifiedOneToOneRelationship";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRelationship obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdRelAssignsPropertyWithValues) {
            return schema.getObjectType(XtdRelActsUpon.LABEL);
        }

        throw new NotImplementedException("Unsupported relationships type: " + obj);
    }
}
