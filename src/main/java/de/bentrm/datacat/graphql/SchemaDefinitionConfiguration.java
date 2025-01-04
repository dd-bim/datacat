package de.bentrm.datacat.graphql;

import de.bentrm.datacat.graphql.fetcher.MutationFetchers;
import de.bentrm.datacat.graphql.resolver.CustomResolver;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class SchemaDefinitionConfiguration implements ResourceLoaderAware {

    private final static String[] schemaFiles = { "classpath:graphql/xtd.graphqls", "classpath:graphql/auth.graphqls",
            "classpath:graphql/admin.graphqls", "classpath:graphql/management.graphqls" };

    private ResourceLoader resourceLoader;

    @Autowired
    private List<CustomResolver> customResolvers;

    @Autowired
    private List<MutationFetchers> mutationFetchers;

    final SchemaParser schemaParser = new SchemaParser();
    final SchemaGenerator schemaGenerator = new SchemaGenerator();
    final TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    final RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

    @ExceptionHandler(BadCredentialsException.class)
    GraphQLError handle(Throwable e) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(e.getMessage(), "Catch all handler").build();
    }

    @Bean
    GraphQLSchema schema() throws IOException {
        parseSchema();

        customResolvers.forEach(resolver -> {
            builder.type(resolver.getTypeName(), wiring -> wiring.typeResolver(resolver));
        });

        RuntimeWiring wiring = getRuntimeWiring();

        return schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    private RuntimeWiring getRuntimeWiring() {

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
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public BufferedInputStream getSchemaFileImputStream(String location) throws IOException {
        final Resource resource = resourceLoader.getResource(location);
        log.trace("Classpath resource {} available: {}", location, resource.exists());
        final InputStream inputStream = resource.getInputStream();
        return new BufferedInputStream(inputStream);
    }
}
