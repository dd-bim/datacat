package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CatalogRecordResolver implements CustomResolver {

    private final XtdRootResolver rootTypeResolver;

    public CatalogRecordResolver(@Qualifier("xtdRootResolver") XtdRootResolver rootTypeResolver) {
        this.rootTypeResolver = rootTypeResolver;
    }

    @Override
    public String getTypeName() {
        return "CatalogRecord";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        CatalogRecord obj = env.getObject();

        if (obj instanceof XtdRoot) {
            return rootTypeResolver.getType(env);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
