package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("xtdRootResolver")
public class XtdRootResolver implements CustomResolver {

    @Autowired
    private XtdObjectResolver objectTypeResolver;

    @Override
    public String getTypeName() {
        return "XtdRoot";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        XtdRoot obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdObject) {
            return objectTypeResolver.getType(env);
        }
        if (obj instanceof XtdLanguage) {
            return schema.getObjectType(XtdLanguage.LABEL);
        }
        if (obj instanceof XtdDictionary) {
            return schema.getObjectType(XtdDictionary.LABEL);
        }
        if (obj instanceof XtdInterval) {
            return schema.getObjectType(XtdInterval.LABEL);
        }
        if (obj instanceof XtdMultiLanguageText) {
            return schema.getObjectType(XtdMultiLanguageText.LABEL);
        }
        if (obj instanceof XtdRational) {
            return schema.getObjectType(XtdRational.LABEL);
        }
        if (obj instanceof XtdSymbol) {
            return schema.getObjectType(XtdSymbol.LABEL);
        }
        if (obj instanceof XtdText) {
            return schema.getObjectType(XtdText.LABEL);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
