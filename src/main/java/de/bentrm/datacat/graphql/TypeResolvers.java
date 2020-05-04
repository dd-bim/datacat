package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.resolver.*;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TypeResolvers {

    @Autowired
    private NodeTypeResolver nodeTypeResolver;

    @Autowired
    private XtdLanguageRepresentationTypeResolver languageRepresentationTypeResolver;

    @Autowired
    private XtdEntityTypeResolver entityTypeResolver;

    @Autowired
    private XtdRootTypeResolver rootTypeResolver;

    @Autowired
    private XtdObjectTypeResolver objectTypeResolver;

    @Autowired
    private XtdCollectionTypeResolver collectionTypeResolver;

    @Autowired
    private XtdRelationshipTypeResolver relationshipTypeResolver;

    public void mapTypeResolvers(RuntimeWiring.Builder builder) {
        builder
                .type("Node", typeWiring -> typeWiring.typeResolver(nodeTypeResolver))
                .type("XtdLanguageRepresentation", typeWiring -> typeWiring.typeResolver(languageRepresentationTypeResolver))
                .type("XtdEntity", typeWiring -> typeWiring.typeResolver(entityTypeResolver))
                .type("XtdRoot", typeWiring -> typeWiring.typeResolver(rootTypeResolver))
                .type("XtdObject", typeWiring -> typeWiring.typeResolver(objectTypeResolver))
                .type("XtdCollection", typeWiring -> typeWiring.typeResolver(collectionTypeResolver))
                .type("XtdRelationship", typeWiring -> typeWiring.typeResolver(relationshipTypeResolver));
    }

}
