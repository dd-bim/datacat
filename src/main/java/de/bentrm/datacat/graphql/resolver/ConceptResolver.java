package de.bentrm.datacat.graphql.resolver;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConceptResolver implements CustomResolver {

    @Autowired
    private XtdRootResolver rootTypeResolver;

    @Autowired
    private XtdRelationshipResolver relationshipTypeResolver;

    @Override
    public String getTypeName() {
        return "Concept";
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        CatalogItem obj = env.getObject();
        GraphQLSchema schema = env.getSchema();

        if (obj instanceof XtdExternalDocument) {
            return schema.getObjectType(XtdExternalDocument.LABEL);
        }
        if (obj instanceof XtdRoot) {
            return rootTypeResolver.getType(env);
        }
        if (obj instanceof XtdRelationship) {
            return relationshipTypeResolver.getType(env);
        }

        throw new NotImplementedException("Unable to resolve type " + obj);
    }
}
