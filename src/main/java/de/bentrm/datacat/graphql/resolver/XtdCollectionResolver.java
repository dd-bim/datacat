package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.domain.XtdNest;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdCollectionResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "XtdCollection";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();

        if (obj instanceof XtdBag) {
            return env.getSchema().getObjectType(XtdBag.LABEL);
        } else if (obj instanceof XtdNest) {
            return env.getSchema().getObjectType(XtdNest.LABEL);
        }

        throw new NotImplementedException("Unsupported collection type: " + obj);
    }
}
