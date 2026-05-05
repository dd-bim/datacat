package de.bentrm.datacat.graphql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.graphql.execution.DefaultBatchLoaderRegistry;

/**
 * GraphQL configuration for optimizing batch loading and performance.
 * Addresses connection pool issues when querying large datasets (1000+ items).
 */
@Configuration
public class GraphQLConfig {

    /**
     * Configure BatchLoaderRegistry for large datasets.
     * This helps prevent "Connection pool pending acquisition queue is full" errors.
     * Note: Batch loading options (batch size, scheduling delay) should be configured
     * when registering individual batch loaders using DataLoaderOptions.
     */
    @Bean
    public BatchLoaderRegistry batchLoaderRegistry() {
        return new DefaultBatchLoaderRegistry();
    }
}
