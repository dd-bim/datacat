package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdRoot;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XtdEntityTypeResolver implements TypeResolver {

    @Autowired
    private XtdRootTypeResolver rootTypeResolver;

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdEntity obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        System.out.println(String.format("Resolving: %s", obj));

        if (obj instanceof XtdExternalDocument) {
            return schema.getObjectType(XtdExternalDocument.LABEL);
        }
        if (obj instanceof XtdRoot) {
            return rootTypeResolver.getType(env);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
