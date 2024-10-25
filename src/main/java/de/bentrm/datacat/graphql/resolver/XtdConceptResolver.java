package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.*;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class XtdConceptResolver implements CustomResolver {

    @Override
    public String getTypeName() {
        return "XtdConcept";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdValueList) {
            return schema.getObjectType(XtdValueList.LABEL);
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
        // if (obj instanceof XtdRelationshipToProperty) {
        //     return schema.getObjectType(XtdRelationshipToProperty.LABEL);
        // }
        if (obj instanceof XtdRelationshipType) {
            return schema.getObjectType(XtdRelationshipType.LABEL);
        }
        if (obj instanceof XtdExternalDocument) {
            return schema.getObjectType(XtdExternalDocument.LABEL);
        }
        if (obj instanceof XtdDimension) {
            return schema.getObjectType(XtdDimension.LABEL);
        }
        if (obj instanceof XtdQuantityKind) {
            return schema.getObjectType(XtdQuantityKind.LABEL);
        }
        if (obj instanceof XtdMultiLanguageText) {
            return schema.getObjectType(XtdMultiLanguageText.LABEL);
        }
        if (obj instanceof XtdCountry) {
            return schema.getObjectType(XtdCountry.LABEL);
        }
        if (obj instanceof XtdSubdivision) {
            return schema.getObjectType(XtdSubdivision.LABEL);
        }
        throw new NotImplementedException("Unsupported type: " + obj);
    }
}
