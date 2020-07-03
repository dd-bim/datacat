package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.*;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

@Slf4j
@Configuration
public class SchemaDefinitionConfiguration implements ResourceLoaderAware {

    private final static String[] schemaFiles = {
            "classpath:graphql/xtd.graphqls",
            "classpath:graphql/auth.graphqls",
            "classpath:graphql/admin.graphqls",
            "classpath:graphql/query.graphqls",
            "classpath:graphql/mutation.graphqls"
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

    final SchemaParser schemaParser = new SchemaParser();
    final SchemaGenerator schemaGenerator = new SchemaGenerator();
    final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

    final UnaryOperator<TypeRuntimeWiring.Builder> rootFetchers = typeWiring -> {
        rootDataFetcherProviders.forEach(provider -> {
            final Map<String, DataFetcher> fetchers = provider.getRootDataFetchers();
            typeWiring.dataFetchers(fetchers);
        });
        return typeWiring;
    };
    final UnaryOperator<TypeRuntimeWiring.Builder> objectFetchers = typeWiring -> {
        rootFetchers.apply(typeWiring);
        objectDataFetcherProviders.forEach(provider -> {
            final Map<String, DataFetcher> fetchers = provider.getObjectDataFetchers();
            typeWiring.dataFetchers(fetchers);
        });
        return typeWiring;
    };

    @Bean
    GraphQLSchema schema() throws IOException {
        parseSchema();
        typeResolvers.mapTypeResolvers(builder);
        RuntimeWiring wiring = getRuntimeWiring();
        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    private RuntimeWiring getRuntimeWiring() {
        return builder
                .type("Translation", typeWiring -> typeWiring.dataFetcher("language", baseDataFetcherProvider.languageByLanguageRepresentation()))
                .type("XtdExternalDocument", typeWiring -> typeWiring
                        .dataFetchers(externalDocumentDataFetcherProvider.getDataFetchers()))
                .type("XtdActivity", objectFetchers)
                .type("XtdActor", objectFetchers)
                .type("XtdClassification", objectFetchers)
                .type("XtdSubject", objectFetchers)
                .type("XtdUnit", objectFetchers)
                .type("XtdProperty", objectFetchers)
                .type("XtdValue", objectFetchers)
                .type("XtdMeasureWithUnit", typeWiring -> objectFetchers
                        .apply(typeWiring)
                        .dataFetchers(measureWithUnitDataFetcherProvider.getPropertyDataFetchers()))
                .type("XtdBag", rootFetchers)
                .type("XtdNest", rootFetchers)
                .type("XtdRelAssignsCollections", rootFetchers)
                .type("XtdRelAssignsPropertyWithValues", rootFetchers)
                .type("XtdRelDocuments", rootFetchers)
                .type("XtdRelCollects", rootFetchers)
                .type("XtdRelAssociates", typeWiring -> rootFetchers
                        .apply(typeWiring)
                        .dataFetchers(relAssociatesProvider.getRelAssociatesDataFetchers()))
                .type("XtdRelGroups", typeWiring -> rootFetchers
                        .apply(typeWiring)
                        .dataFetchers(relGroupsProvider.getRelGroupsDataFetchers()))
                .type("XtdRelSpecializes", typeWiring -> rootFetchers
                        .apply(typeWiring)
                        .dataFetchers(relSpecializesProvider.getRelSpecializesDataFetchers()))
                .type("XtdRelComposes", typeWiring -> rootFetchers
                        .apply(typeWiring)
                        .dataFetchers(relComposesProvider.getRelComposesDataFetchers()))
                .type("XtdRelActsUpon", typeWiring -> rootFetchers
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
    }

    private void parseSchema() throws IOException {
        for (String location : schemaFiles) {
            final BufferedInputStream inputStream = getSchemaFileImputStream(location);
            log.debug("Parsing GraphQL schema file {}", location);
            final TypeDefinitionRegistry subRegistry = schemaParser.parse(inputStream);
            typeRegistry.merge(subRegistry);
        }
    }

    @Override
    public void setResourceLoader(@NotNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public BufferedInputStream getSchemaFileImputStream(String location) throws IOException {
        final Resource resource = resourceLoader.getResource(location);
        log.debug("Classpath resource {} available: {}", location, resource.exists());
        final InputStream inputStream = resource.getInputStream();
        return new BufferedInputStream(inputStream);
    }
}
