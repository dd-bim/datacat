package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.*;
import de.bentrm.datacat.graphql.resolver.*;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
public class SchemaDefinition implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

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

    @Autowired
    private XtdDataFetchers dataFetchers;

    @Autowired
    private ActorDataFetcherProvider actorProvider;

    @Autowired
    private ActivityDataFetcherProvider activityProvider;

    @Autowired
    private ClassificationDataFetcherProvider classificationProvider;

    @Autowired
    private PropertyDataFetcherProvider propertyProvider;

    @Autowired
    private SubjectDataFetcherProvider subjectProvider;

    @Autowired
    private UnitDataFetcherProvider unitProvider;

    @Autowired
    private ValueDataFetcherProvider valueProvider;

    @Autowired
    private MeasureWithUnitDataFetcherProvider measureProvider;

    @Autowired
    private RelAssociatesDataFetcherProvider relAssociatesProvider;

    @Autowired
    private RelGroupsDataFetcherProvider relGroupsProvider;

    @Autowired
    private RelSpecializesDataFetcherProvider relSpecializesProvider;

    @Autowired
    private RelComposesDataFetcherProvider relComposesProvider;

    @Autowired
    private SearchDataFetcherProvider searchProvider;

    @Bean
    GraphQLSchema schema() throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStream input = loadSchema("classpath:schema.graphqls").getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(reader);
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("XtdLanguageRepresentation", typeWiring -> typeWiring.typeResolver(languageRepresentationTypeResolver))
                .type("XtdName", typeWiring -> typeWiring.dataFetcher("languageName", dataFetchers.languageByLanguageRepresentation()))
                .type("XtdDescription", typeWiring -> typeWiring.dataFetcher("languageName", dataFetchers.languageByLanguageRepresentation()))
                .type("XtdEntity", typeWiring -> typeWiring.typeResolver(entityTypeResolver))
                .type("XtdRoot", typeWiring -> typeWiring.typeResolver(rootTypeResolver))
                .type("XtdObject", typeWiring -> typeWiring.typeResolver(objectTypeResolver))
                .type("XtdActivity", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers()))
                .type("XtdActor", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers()))
                .type("XtdSubject", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers()))
                .type("XtdUnit", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers()))
                .type("XtdProperty", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers()))
                .type("XtdCollection", typeWiring -> typeWiring.typeResolver(collectionTypeResolver))
                .type("XtdRelationship", typeWiring -> typeWiring.typeResolver(relationshipTypeResolver))
                .type("XtdRelAssociates", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers())
                        .dataFetchers(relAssociatesProvider.getRelAssociatesDataFetchers()))
                .type("XtdRelGroups", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRelGroupsDataFetchers()))
                .type("XtdRelSpecializes", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRelSpecializesDataFetchers()))
                .type("XtdRelComposes", typeWiring -> typeWiring
                        .dataFetchers(relAssociatesProvider.getRootDataFetchers())
                        .dataFetchers(relGroupsProvider.getRootDataFetchers())
                        .dataFetchers(relSpecializesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRootDataFetchers())
                        .dataFetchers(relComposesProvider.getRelComposesDataFetchers()))
                .type("Query", typeWiring -> typeWiring
                        .dataFetchers(searchProvider.getQueryDataFetchers())
                        .dataFetcher("document", dataFetchers.externalDocumentById())
                        .dataFetcher("documents", dataFetchers.externalDocumentBySearch())
                        .dataFetchers(actorProvider.getQueryDataFetchers())
                        .dataFetchers(activityProvider.getQueryDataFetchers())
                        .dataFetchers(classificationProvider.getQueryDataFetchers())
                        .dataFetchers(propertyProvider.getQueryDataFetchers())
                        .dataFetchers(subjectProvider.getQueryDataFetchers())
                        .dataFetchers(unitProvider.getQueryDataFetchers())
                        .dataFetchers(valueProvider.getQueryDataFetchers())
                        .dataFetchers(measureProvider.getQueryDataFetchers())
                        .dataFetchers(relAssociatesProvider.getQueryDataFetchers())
                        .dataFetchers(relGroupsProvider.getQueryDataFetchers())
                        .dataFetchers(relSpecializesProvider.getQueryDataFetchers())
                        .dataFetchers(relComposesProvider.getQueryDataFetchers()))
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("createDocument", dataFetchers.createExternalDocument())
                        .dataFetcher("deleteDocument", dataFetchers.deleteExternalDocument())
                        .dataFetchers(actorProvider.getMutationDataFetchers())
                        .dataFetchers(activityProvider.getMutationDataFetchers())
                        .dataFetchers(classificationProvider.getMutationDataFetchers())
                        .dataFetchers(propertyProvider.getMutationDataFetchers())
                        .dataFetchers(subjectProvider.getMutationDataFetchers())
                        .dataFetchers(unitProvider.getMutationDataFetchers())
                        .dataFetchers(valueProvider.getMutationDataFetchers())
                        .dataFetchers(measureProvider.getMutationDataFetchers())
                        .dataFetchers(relAssociatesProvider.getMutationDataFetchers())
                        .dataFetchers(relGroupsProvider.getMutationDataFetchers())
                        .dataFetchers(relSpecializesProvider.getMutationDataFetchers())
                        .dataFetchers(relComposesProvider.getMutationDataFetchers()))
                .build();
        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    @Override
    public void setResourceLoader(@NotNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource loadSchema(String location) {
        return resourceLoader.getResource(location);
    }
}
