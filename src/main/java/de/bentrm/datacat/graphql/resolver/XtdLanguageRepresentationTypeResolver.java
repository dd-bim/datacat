package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.domain.XtdDescription;
import de.bentrm.datacat.domain.XtdName;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdLanguageRepresentationTypeResolver implements TypeResolver {
    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();

        if (obj instanceof XtdName) {
            return env.getSchema().getObjectType(XtdName.LABEL);
        }
        if (obj instanceof XtdDescription) {
            return env.getSchema().getObjectType(XtdDescription.LABEL);
        }

        throw new NotImplementedException("Unsupported type: " + obj);
    }
}
