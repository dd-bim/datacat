package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.*;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
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
import java.util.List;
import java.util.function.UnaryOperator;

@Configuration
public class SchemaDefinition implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Autowired
    private TypeResolvers typeResolvers;

    @Autowired
    private BaseDataFetcherProvider dataFetchers;

    @Autowired
    private List<QueryDataFetcherProvider> queryDataFetcherProviders;

    @Autowired
    private List<RootDataFetcherProvider> rootDataFetcherProviders;

    @Autowired
    private List<MutationDataFetcherProvider> mutationDataFetcherProviders;

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
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStream interfaces = loadSchema("classpath:interfaces.graphqls").getInputStream();
        InputStreamReader interfacesInputStream = new InputStreamReader(interfaces);
        BufferedReader interfacesReader = new BufferedReader(interfacesInputStream);
        TypeDefinitionRegistry typeRegistry = schemaParser.parse(interfacesReader);

        InputStream schema = loadSchema("classpath:schema.graphqls").getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(schema);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        typeRegistry.merge(schemaParser.parse(reader));

        final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
        typeResolvers.mapTypeResolvers(builder);
        final UnaryOperator<TypeRuntimeWiring.Builder> rootDataFetchers = typeWiring -> {
            rootDataFetcherProviders.forEach(provider -> typeWiring.dataFetchers(provider.getRootDataFetchers()));
            return typeWiring;
        };

        RuntimeWiring wiring = builder
                .type("XtdName", typeWiring -> typeWiring.dataFetcher("languageName", dataFetchers.languageByLanguageRepresentation()))
                .type("XtdDescription", typeWiring -> typeWiring.dataFetcher("languageName", dataFetchers.languageByLanguageRepresentation()))
                .type("XtdActivity", rootDataFetchers)
                .type("XtdActor", rootDataFetchers)
                .type("XtdSubject", rootDataFetchers)
                .type("XtdUnit", rootDataFetchers)
                .type("XtdProperty", rootDataFetchers)
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
