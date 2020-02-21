package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdLanguageRepresentation;
import de.bentrm.datacat.domain.XtdRoot;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TermTypeResolver implements TypeResolver {

    private final XtdLanguageRepresentationTypeResolver languageRepresentationTypeResolver;

    private final XtdRootTypeResolver rootTypeResolver;

    @Autowired
    public TermTypeResolver(XtdLanguageRepresentationTypeResolver languageRepresentationTypeResolver, XtdRootTypeResolver rootTypeResolver) {
        this.languageRepresentationTypeResolver = languageRepresentationTypeResolver;
        this.rootTypeResolver = rootTypeResolver;
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Entity obj = env.getObject();

        if (obj instanceof XtdLanguageRepresentation) {
            return languageRepresentationTypeResolver.getType(env);
        }
        if (obj instanceof XtdRoot) {
            return rootTypeResolver.getType(env);
        }

        throw new NotImplementedException("Check additional types needed for GraphQL API: " + obj);
    }
}
