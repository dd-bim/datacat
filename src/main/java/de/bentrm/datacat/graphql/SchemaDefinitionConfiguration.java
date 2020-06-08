package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.*;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
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
import java.util.List;
import java.util.function.UnaryOperator;

@Configuration
public class SchemaDefinitionConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Autowired
    private Logger logger;

    @Autowired
    private TypeResolvers typeResolvers;

    @Autowired
    private BaseDataFetcherProvider baseDataFetcherProvider;

    @Autowired
    private ExternalDocumentDataFetcherProvider externalDocumentDataFetcherProvider;

    @Autowired
    private List<QueryDataFetcherProvider> queryDataFetcherProviders;

    @Autowired
    private List<RootDataFetcherProvider> rootDataFetcherProviders;

    @Autowired
    private List<ObjectDataFetcherProvider> objectDataFetcherProviders;

    @Autowired
    private List<MutationDataFetcherProvider> mutationDataFetcherProviders;

    @Autowired
    private RelDocumentsDataFetcherProvider relDocumentsProvider;

    @Autowired
    private RelCollectsDataFetcherProvider relCollectsProvider;

    @Autowired
    private RelAssociatesDataFetcherProvider relAssociatesProvider;

    @Autowired
    private RelGroupsDataFetcherProvider relGroupsProvider;

    @Autowired
    private RelSpecializesDataFetcherProvider relSpecializesProvider;

    @Autowired
    private RelComposesDataFetcherProvider relComposesProvider;

    @Autowired
    private RelActsUponDataFetcherProvider relActsUponProvider;

    @Bean
    GraphQLSchema schema() throws IOException {
        final SchemaParser schemaParser = new SchemaParser();
        final SchemaGenerator schemaGenerator = new SchemaGenerator();
        final TypeDefinitionRegistry typeRegistry;
        final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
        final UnaryOperator<TypeRuntimeWiring.Builder> rootDataFetchers = typeWiring -> {
            rootDataFetcherProviders.forEach(provider -> typeWiring.dataFetchers(provider.getRootDataFetchers()));
            return typeWiring;
        };
        final UnaryOperator<TypeRuntimeWiring.Builder> objectDataFetchers = typeWiring -> {
            rootDataFetchers.apply(typeWiring);
            objectDataFetcherProviders.forEach(provider -> typeWiring.dataFetchers(provider.getObjectDataFetchers()));
            return typeWiring;
        };

        try {
            InputStream inputStream = loadSchema("classpath:xtd.graphqls").getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            typeRegistry = schemaParser.parse(bufferedReader);
        } catch (IOException e) {
            logger.debug("Unabled to load XTD GraphQL schema file.");
            throw e;
        }

        try {
            InputStream inputStream = loadSchema("classpath:query.graphqls").getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            typeRegistry.merge(schemaParser.parse(bufferedReader));
        } catch (IOException e) {
            logger.debug("Unabled to load Query GraphQL schema file.");
            throw e;
        }

        try {
            InputStream inputStream = loadSchema("classpath:mutation.graphqls").getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            typeRegistry.merge(schemaParser.parse(bufferedReader));
        } catch (IOException e) {
            logger.debug("Unabled to load Mutation GraphQL schema file.");
            throw e;
        }

        typeResolvers.mapTypeResolvers(builder);

        RuntimeWiring wiring = builder
                .type("XtdName", typeWiring -> typeWiring.dataFetcher("language", baseDataFetcherProvider.languageByLanguageRepresentation()))
                .type("XtdDescription", typeWiring -> typeWiring.dataFetcher("language", baseDataFetcherProvider.languageByLanguageRepresentation()))
                .type("XtdExternalDocument", typeWiring -> typeWiring
                        .dataFetchers(externalDocumentDataFetcherProvider.getDataFetchers()))
                .type("XtdActivity", objectDataFetchers)
                .type("XtdActor", objectDataFetchers)
                .type("XtdSubject", objectDataFetchers)
                .type("XtdUnit", objectDataFetchers)
                .type("XtdProperty", objectDataFetchers)
                .type("XtdValue", objectDataFetchers)
                .type("XtdBag", rootDataFetchers)
                .type("XtdNest", rootDataFetchers)
                .type("XtdRelDocuments", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relDocumentsProvider.getRelDocumentsDataFetchers()))
                .type("XtdRelCollects", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relCollectsProvider.getRelCollectsDataFetchers()))
                .type("XtdRelAssociates", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relAssociatesProvider.getRelAssociatesDataFetchers()))
                .type("XtdRelGroups", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relGroupsProvider.getRelGroupsDataFetchers()))
                .type("XtdRelSpecializes", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relSpecializesProvider.getRelSpecializesDataFetchers()))
                .type("XtdRelComposes", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relComposesProvider.getRelComposesDataFetchers()))
                .type("XtdRelActsUpon", typeWiring -> rootDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(relActsUponProvider.getRelActsUponDataFetchers()))
                .type("Query", typeWiring -> {
                    queryDataFetcherProviders.forEach(provider -> typeWiring.dataFetchers(provider.getQueryDataFetchers()));
                    return typeWiring;
                })
                .type("Mutation", typeWiring -> {
                    mutationDataFetcherProviders.forEach(provider -> typeWiring.dataFetchers(provider.getMutationDataFetchers()));
                    return typeWiring;
                })
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
