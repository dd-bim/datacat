package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.springframework.stereotype.Component;

@Component
public class SimpleRecordResolver extends XtdRootResolver {

    @Override
    public String getTypeName() {
        return "SimpleRecord";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        // if (obj instanceof XtdExternalDocument) {
        //     return schema.getObjectType(XtdExternalDocument.LABEL);
        // }
        return super.getType(env);
    }
}
