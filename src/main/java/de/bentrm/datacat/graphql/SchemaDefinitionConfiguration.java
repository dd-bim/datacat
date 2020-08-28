package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.AttributeFetchers;
import de.bentrm.datacat.graphql.fetcher.MutationFetchers;
import de.bentrm.datacat.graphql.fetcher.QueryFetchers;
import graphql.GraphQLError;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    private List<AttributeFetchers> attributeFetchers;

    @Autowired
    private List<QueryFetchers> queryFetchers;

    @Autowired
    private List<MutationFetchers> mutationFetchers;

    final SchemaParser schemaParser = new SchemaParser();
    final SchemaGenerator schemaGenerator = new SchemaGenerator();
    final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

    @ExceptionHandler(BadCredentialsException.class)
    GraphQLError handle(Throwable e) {
        return new ThrowableGraphQLError(e, "Catch all handler");
    }

    @Bean
    GraphQLSchema schema() throws IOException {
        parseSchema();
        typeResolvers.mapTypeResolvers(builder);
        RuntimeWiring wiring = getRuntimeWiring();
        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    private RuntimeWiring getRuntimeWiring() {
        attributeFetchers.forEach(fetcher -> {
            log.trace("Registering AttributeFetcher {}", fetcher.getClass());
            builder.type(fetcher.getTypeName(), wiring -> wiring.dataFetchers(fetcher.getAttributeFetchers()));
        });

        builder.type("Query", wiring -> {
            queryFetchers.forEach(fetcher -> {
                log.trace("Registering QueryFetchers {}", fetcher.getClass());
                wiring.dataFetchers(fetcher.getQueryFetchers());
            });
            return wiring;
        });

        builder.type("Mutation", wiring -> {
            mutationFetchers.forEach(fetcher -> {
                log.trace("Registering MutationFetchers {}", fetcher.getClass());
                wiring.dataFetchers(fetcher.getMutationFetchers());
            });
            return wiring;
        });

        return builder.build();
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
        log.trace("Classpath resource {} available: {}", location, resource.exists());
        final InputStream inputStream = resource.getInputStream();
        return new BufferedInputStream(inputStream);
    }
}
