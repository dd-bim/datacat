package de.bentrm.datacat.graphql;

import de.bentrm.datacat.domain.*;
import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.domain.relationship.XtdRelationship;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static graphql.Scalars.*;
import static graphql.schema.GraphQLCodeRegistry.newCodeRegistry;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLInterfaceType.newInterface;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLNonNull.nonNull;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLTypeReference.typeRef;

@Configuration
public class SchemaDefinition {
    private GraphQLObjectType page = newObject()
            .name("Page")
            .field(field -> field.name("pageNumber").type(nonNull(GraphQLInt)))
            .field(field -> field.name("pageSize").type(nonNull(GraphQLInt)))
            .field(field -> field.name("totalElements").type(nonNull(GraphQLInt)))
            .field(field -> field.name("totalPages").type(nonNull(GraphQLInt)))
            .field(field -> field.name("hasNext").type(nonNull(GraphQLBoolean)))
            .field(field -> field.name("hasPrevious").type(nonNull(GraphQLBoolean)))
            .field(field -> field.name("isFirst").type(nonNull(GraphQLBoolean)))
            .field(field -> field.name("isLast").type(nonNull(GraphQLBoolean)))
            .build();

    private GraphQLInterfaceType entity = newInterface()
            .name("Entity")
            .field(field -> field.name("id").type(nonNull(GraphQLString)))
            .field(field -> field.name("uniqueId").type(nonNull(GraphQLID)))
            .field(field -> field.name("created").type(nonNull(GraphQLString)))
            .field(field -> field.name("lastModified").type(nonNull(GraphQLString)))
            .build();

    private GraphQLObjectType xtdLanguage = newObject()
            .name(XtdLanguage.LABEL)
            .fields(entity.getFieldDefinitions())
            .field(field -> field.name("languageCode").type(nonNull(GraphQLString)))
            .field(field -> field.name("languageNameInEnglish").type(nonNull(GraphQLString)))
            .field(field -> field.name("languageNameInSelf").type(GraphQLString))
            .build();

    private GraphQLInterfaceType xtdLanguageRepresentation = newInterface(entity)
            .name(XtdLanguageRepresentation.LABEL)
            .field(field -> field.name("languageName").type(nonNull(typeRef(XtdLanguage.LABEL))))
            .build();

    private GraphQLObjectType xtdName = newObject()
            .name(XtdName.LABEL)
            .withInterfaces(xtdLanguageRepresentation)
            .fields(xtdLanguageRepresentation.getFieldDefinitions())
            .field(field -> field.name("name").type(nonNull(GraphQLString)))
            .build();

    private GraphQLFieldDefinition labelField = newFieldDefinition()
            .name("label")
            .type(nonNull(GraphQLString))
            .build();

    private GraphQLFieldDefinition namesField = newFieldDefinition()
            .name("names")
            .type(nonNull(list(nonNull(xtdName))))
            .build();

    private GraphQLObjectType xtdDescription = newObject()
            .name(XtdDescription.LABEL)
            .withInterfaces(xtdLanguageRepresentation)
            .fields(xtdLanguageRepresentation.getFieldDefinitions())
            .field(field -> field.name("description").type(nonNull(GraphQLString)))
            .build();

    private GraphQLFieldDefinition descriptionsField = newFieldDefinition()
            .name("descriptions")
            .type(list(nonNull(xtdDescription)))
            .build();

    GraphQLObjectType xtdDocument = newObject()
            .name(XtdExternalDocument.LABEL)
            .fields(entity.getFieldDefinitions())
            .field(labelField)
            .field(namesField)
            .field(field -> field.name("documents").type(nonNull(typeRef(XtdRelDocuments.LABEL + "Connection"))))
            .build();

    GraphQLInterfaceType xtdRoot = newInterface(entity)
            .name(XtdRoot.LABEL)
            .fields(entity.getFieldDefinitions())
            .field(labelField)
            .field(namesField)
            .field(descriptionsField)
            .field(field -> field.name("versionId").type(GraphQLString))
            .field(field -> field.name("versionDate").type(GraphQLString))
            .field(field -> field.name("groups").type(nonNull(typeRef(XtdRelGroups.LABEL + "Connection"))))
            .build();

    GraphQLInterfaceType xtdObject = newInterface(xtdRoot).name(XtdObject.LABEL).build();
    GraphQLInterfaceType xtdCollection = newInterface(xtdRoot).name(XtdCollection.LABEL).build();
    GraphQLInterfaceType xtdRelationship = newInterface(xtdRoot).name(XtdRelationship.LABEL).build();

    GraphQLObjectType xtdActivity = newObject()
            .name(XtdActivity.LABEL)
            .withInterfaces(xtdRoot, xtdObject)
            .fields(xtdObject.getFieldDefinitions())
            .build();
    GraphQLObjectType xtdActor = newObject(xtdActivity).name(XtdActor.LABEL).build();
    GraphQLObjectType xtdClassification = newObject(xtdActivity).name(XtdClassification.LABEL).build();
    GraphQLObjectType xtdMeasureWithUnit = newObject(xtdActivity).name(XtdMeasureWithUnit.LABEL).build();
    GraphQLObjectType xtdProperty = newObject(xtdActivity).name(XtdProperty.LABEL).build();
    GraphQLObjectType xtdSubject = newObject(xtdActivity).name(XtdSubject.LABEL).build();
    GraphQLObjectType xtdUnit = newObject(xtdActivity).name(XtdUnit.LABEL).build();
    GraphQLObjectType xtdValue = newObject(xtdActivity).name(XtdValue.LABEL).build();

    GraphQLObjectType xtdBag = newObject()
            .name(XtdBag.LABEL)
            .withInterfaces(xtdRoot, xtdCollection)
            .fields(xtdCollection.getFieldDefinitions())
            .build();
    GraphQLObjectType xtdNest = newObject(xtdBag).name(XtdNest.LABEL).build();

    GraphQLObjectType xtdRelDocuments = newObject()
            .name(XtdRelDocuments.LABEL)
            .withInterfaces(xtdRoot, xtdRelationship)
            .fields(xtdRelationship.getFieldDefinitions())
            .field(field -> field.name("relatingDocument").type(nonNull(xtdDocument)))
            .field(field -> field
                    .name("relatedThings")
                    .type(nonNull(connection(xtdObject)))
                    .argument(arg -> arg.name("options").type(typeRef("SearchOptions"))))
            .build();

    GraphQLObjectType xtdRelAssociates = newObject()
            .name(XtdRelAssociates.LABEL)
            .withInterfaces(xtdRoot, xtdRelationship)
            .fields(xtdRelationship.getFieldDefinitions())
            .field(field -> field.name("relatingObject").type(nonNull(xtdObject)))
            .field(field -> field
                    .name("relatedObjects")
                    .type(nonNull(typeRef(XtdObject.LABEL + "Connection")))
                    .argument(arg -> arg.name("options").type(typeRef("SearchOptions"))))
            .build();

    GraphQLObjectType xtdRelGroups = newObject(xtdRelAssociates)
            .name(XtdRelGroups.LABEL)
            .build();

    private GraphQLInputObjectType searchOptions = newInputObject()
            .name("SearchOptions")
            .field(field -> field.name("term").type(GraphQLString))
            .field(field -> field.name("pageSize").type(GraphQLInt))
            .field(field -> field.name("pageNumber").type(GraphQLInt))
            .build();

    private GraphQLInputObjectType relGroupsSearchOptions = newInputObject(searchOptions)
            .name("XtdRelGroupsSearchOptions")
            .field(field -> field.name("relatingObject").type(GraphQLID))
            .build();

    private GraphQLInputObjectType xtdNameInput = newInputObject()
            .name(XtdName.LABEL + "Input")
            .field(field -> field.name("uniqueId").type(GraphQLID))
            .field(field -> field.name("languageCode").type(nonNull(GraphQLString)))
            .field(field -> field.name("name").type(nonNull(GraphQLString)))
            .field(field -> field.name("ignoreDuplicate").type(GraphQLBoolean).defaultValue(false))
            .build();

    private GraphQLInputObjectType xtdAddNameInput = newInputObject(xtdNameInput)
            .name("add" + XtdName.LABEL + "Input")
            .field(field -> field.name("sortOrder").type(GraphQLInt))
            .build();

    private GraphQLInputObjectType xtdDescriptionInput = newInputObject()
            .name(XtdDescription.LABEL + "Input")
            .field(field -> field.name("uniqueId").type(GraphQLID))
            .field(field -> field.name("languageCode").type(nonNull(GraphQLString)))
            .field(field -> field.name("description").type(nonNull(GraphQLString)))
            .build();

    private GraphQLInputObjectType xtdAddDescriptionInput = newInputObject(xtdDescriptionInput)
            .name("add" + XtdDescription.LABEL + "Input")
            .field(field -> field.name("sortOrder").type(GraphQLInt))
            .build();

    private GraphQLInputObjectType xtdRootInput = newInputObject()
            .name("RootInput")
            .field(field -> field.name("uniqueId").type(GraphQLID))
            .field(field -> field.name("versionId").type(GraphQLString))
            .field(field -> field.name("versionDate").type(GraphQLString))
            .field(field -> field.name("names").type(nonNull(list(nonNull(xtdNameInput)))))
            .field(field -> field.name("descriptions").type(list(nonNull(xtdDescriptionInput))))
            .build();

    @Bean
    GraphQLSchema schema(GraphQLCodeRegistry codeRegistry) {
        GraphQLObjectType.Builder queryTypeBuilder = newObject().name("query");

        xtdObjectQuery(queryTypeBuilder, "language", "languages", xtdLanguage);
        xtdObjectQuery(queryTypeBuilder, "document", "documents", xtdDocument);
        xtdObjectQuery(queryTypeBuilder, "actor", "actors", xtdActor);
        xtdObjectQuery(queryTypeBuilder, "activity", "activities", xtdActivity);
        xtdObjectQuery(queryTypeBuilder, "classification", "classifications", xtdClassification);
        xtdObjectQuery(queryTypeBuilder, "measure", "measures", xtdMeasureWithUnit);
        xtdObjectQuery(queryTypeBuilder, "property", "properties", xtdProperty);
        xtdObjectQuery(queryTypeBuilder, "subject", "subjects", xtdSubject);
        xtdObjectQuery(queryTypeBuilder, "unit", "units", xtdUnit);
        xtdObjectQuery(queryTypeBuilder, "value", "values", xtdValue);
        xtdObjectQuery(queryTypeBuilder, "bag", "bags", xtdBag);
        xtdObjectQuery(queryTypeBuilder, "nest", "nests", xtdNest);
        xtdObjectQuery(queryTypeBuilder, "documentsRelationship", "documentsRelationships", xtdRelDocuments);

        queryTypeBuilder.field(field -> field.name("groupsRelationship")
                .type(xtdRelGroups)
                .argument(arg -> arg.name("uniqueId").type(GraphQLID)))
                .field(field -> field.name("groupsRelationships")
                        .type(nonNull(connection(xtdRelGroups)))
                        .argument(arg -> arg.name("options").type(relGroupsSearchOptions)));

        GraphQLInputObjectType xtdLanguageInput = newInputObject()
                .name(XtdLanguage.LABEL + "Input")
                .field(field -> field.name("uniqueId").type(GraphQLID))
                .field(field -> field.name("languageCode").type(nonNull(GraphQLString)))
                .field(field -> field.name("languageNameInEnglish").type(nonNull(GraphQLString)))
                .field(field -> field.name("languageNameInSelf").type(GraphQLString))
                .build();

        GraphQLInputObjectType xtdDocumentInput = newInputObject()
                .name(XtdExternalDocument.LABEL + "Input")
                .field(field -> field.name("uniqueId").type(GraphQLID))
                .field(field -> field.name("names").type(nonNull(list(nonNull(xtdNameInput)))))
                .build();

        GraphQLObjectType.Builder mutationTypeBuilder = newObject()
                .name("mutation")
                .field(field -> field.name("addName")
                        .type(xtdObject)
                        .argument(arg -> arg.name("parentUniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("newName").type(nonNull(xtdAddNameInput))))
                .field(field -> field.name("updateName")
                        .type(xtdObject)
                        .argument(arg -> arg.name("parentUniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("uniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("newName").type(nonNull(GraphQLString))))
                .field(field -> field.name("deleteName")
                        .type(xtdObject)
                        .argument(arg -> arg.name("parentUniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("uniqueId").type(nonNull(GraphQLID))))
                .field(field -> field.name("addDescription")
                        .type(xtdObject)
                        .argument(arg -> arg.name("parentUniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("newDescription").type(nonNull(xtdAddDescriptionInput))))
                .field(field -> field.name("updateDescription")
                        .type(xtdObject)
                        .argument(arg -> arg.name("parentUniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("uniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("newDescription").type(nonNull(GraphQLString))))
                .field(field -> field.name("deleteDescription")
                        .type(xtdObject)
                        .argument(arg -> arg.name("parentUniqueId").type(nonNull(GraphQLID)))
                        .argument(arg -> arg.name("uniqueId").type(nonNull(GraphQLID))))
                .field(field -> field.name("addLanguage")
                        .type(xtdLanguage)
                        .argument(arg -> arg.name("newLanguage").type(nonNull(xtdLanguageInput))))
                .field(field -> field.name("addDocument")
                        .type(xtdDocument)
                        .argument(arg -> arg.name("newDocument").type(nonNull(xtdDocumentInput))))
                .field(field -> field.name("deleteDocument")
                        .type(xtdDocument)
                        .argument(arg -> arg.name("uniqueId").type(nonNull(GraphQLID))));

        xtdObjectMutation(mutationTypeBuilder, XtdActor.TITLE, xtdActor);
        xtdObjectMutation(mutationTypeBuilder, XtdActivity.TITLE, xtdActivity);
        xtdObjectMutation(mutationTypeBuilder, XtdClassification.TITLE, xtdClassification);
        xtdObjectMutation(mutationTypeBuilder, XtdMeasureWithUnit.TITLE, xtdMeasureWithUnit);
        xtdObjectMutation(mutationTypeBuilder, XtdProperty.TITLE, xtdProperty);
        xtdObjectMutation(mutationTypeBuilder, XtdSubject.TITLE, xtdSubject);
        xtdObjectMutation(mutationTypeBuilder, XtdUnit.TITLE, xtdUnit);
        xtdObjectMutation(mutationTypeBuilder, XtdValue.TITLE, xtdValue);
        xtdObjectMutation(mutationTypeBuilder, XtdBag.TITLE, xtdBag);
        xtdObjectMutation(mutationTypeBuilder, XtdNest.TITLE, xtdNest);

        GraphQLInputObjectType xtdRelGroupsInput = newInputObject(xtdRootInput)
                .name(xtdRelGroups.getName() + "Input")
                .field(field -> field.name("relatingObjectUniqueId").type(nonNull(GraphQLID)))
                .field(field -> field.name("relatedObjectsUniqueIds").type(nonNull(list(nonNull(GraphQLID)))))
                .build();

        mutationTypeBuilder
                .field(field -> field.name("addGroupsRelationship")
                        .type(xtdRelGroups)
                        .argument(arg -> arg.name("input").type(nonNull(xtdRelGroupsInput))));

        return GraphQLSchema.newSchema()
                .query(queryTypeBuilder.build())
                .mutation(mutationTypeBuilder.build())
                .codeRegistry(codeRegistry)
                .build();
    }

    @Bean
    GraphQLCodeRegistry codeRegistry(
            @Qualifier("xtdCodeRegistry") GraphQLCodeRegistry xtdCodeRegistry,
            @Qualifier("objectCodeRegistry") GraphQLCodeRegistry xtdObjectCodeRegistry,
            @Qualifier("collectionCodeRegistry") GraphQLCodeRegistry xtdCollectionCodeRegistry,
            @Qualifier("relationshipCodeRegistry") GraphQLCodeRegistry xtdRelationshipCodeRegistry) {
        return newCodeRegistry()
                .typeResolvers(xtdCodeRegistry)
                .dataFetchers(xtdCodeRegistry)
                .typeResolvers(xtdObjectCodeRegistry)
                .dataFetchers(xtdObjectCodeRegistry)
                .typeResolvers(xtdCollectionCodeRegistry)
                .dataFetchers(xtdCollectionCodeRegistry)
                .typeResolvers(xtdRelationshipCodeRegistry)
                .dataFetchers(xtdRelationshipCodeRegistry)
                .build();
    }

    private GraphQLObjectType connection(GraphQLOutputType entityObjectType) {
        return newObject()
                .name(entityObjectType.getName() + "Connection")
                .field(field -> field.name("page").type(page))
                .field(field -> field.name("nodes").type(nonNull(list(nonNull(entityObjectType)))))
                .build();
    }

    private void xtdObjectQuery(GraphQLObjectType.Builder builder, String singularName, String pluralName, GraphQLOutputType type) {
        builder
                .field(field -> field.name(singularName)
                        .type(type)
                        .argument(arg -> arg.name("uniqueId").type(GraphQLID)))
                .field(field -> field.name(pluralName)
                        .type(nonNull(connection(type)))
                        .argument(arg -> arg.name("options").type(searchOptions)));
    }

    private void xtdObjectMutation(GraphQLObjectType.Builder builder, String label, GraphQLObjectType type) {
        GraphQLInputObjectType inputType = newInputObject()
                .name(type.getName() + "Input")
                .fields(xtdRootInput.getFieldDefinitions())
                .build();

        builder
                .field(field -> field.name("add" + label)
                        .type(type)
                        .argument(arg -> arg.name("new" + label).type(nonNull(inputType))))
                .field(field -> field.name("delete" + label)
                        .type(type)
                        .argument(arg -> arg.name("uniqueId").type(nonNull(GraphQLID))));
    }
}
