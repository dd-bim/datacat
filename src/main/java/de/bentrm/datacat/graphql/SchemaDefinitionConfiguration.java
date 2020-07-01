package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.*;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.UnaryOperator;

@Configuration
public class SchemaDefinitionConfiguration implements ResourceLoaderAware {

    private final static String[] schemaFiles = {
            "classpath:xtd.graphqls",
            "classpath:query.graphqls",
            "classpath:mutation.graphqls"
    };

    private ResourceLoader resourceLoader;

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
    private MeasureWithUnitDataFetcherProvider measureWithUnitDataFetcherProvider;

    @Autowired
    private RelDocumentsDataFetcherProvider relDocumentsProvider;

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
        final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
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

        for (String location : schemaFiles) {
            final File file = getFile(location);
            final TypeDefinitionRegistry subRegistry = schemaParser.parse(file);
            typeRegistry.merge(subRegistry);
        }

        typeResolvers.mapTypeResolvers(builder);

        RuntimeWiring wiring = builder
                .type("Translation", typeWiring -> typeWiring.dataFetcher("language", baseDataFetcherProvider.languageByLanguageRepresentation()))
                .type("XtdExternalDocument", typeWiring -> typeWiring
                        .dataFetchers(externalDocumentDataFetcherProvider.getDataFetchers()))
                .type("XtdActivity", objectDataFetchers)
                .type("XtdActor", objectDataFetchers)
                .type("XtdClassification", objectDataFetchers)
                .type("XtdSubject", objectDataFetchers)
                .type("XtdUnit", objectDataFetchers)
                .type("XtdProperty", objectDataFetchers)
                .type("XtdValue", objectDataFetchers)
                .type("XtdMeasureWithUnit", typeWiring -> objectDataFetchers
                        .apply(typeWiring)
                        .dataFetchers(measureWithUnitDataFetcherProvider.getPropertyDataFetchers()))
                .type("XtdBag", rootDataFetchers)
                .type("XtdNest", rootDataFetchers)
                .type("XtdRelAssignsCollections", rootDataFetchers)
                .type("XtdRelAssignsPropertyWithValues", rootDataFetchers)
                .type("XtdRelDocuments", rootDataFetchers)
                .type("XtdRelCollects", rootDataFetchers)
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

    public File getFile(String location) throws IOException {
        return resourceLoader.getResource(location).getFile();
    }
}
