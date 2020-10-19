package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.XtdRelSequences;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class OneToOneRelationshipResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "OneToOneRelationship";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdRelSequences) {
            return schema.getObjectType(XtdRelSequences.LABEL);
        }
        throw new NotImplementedException("Unsupported type: " + obj);
    }
}
