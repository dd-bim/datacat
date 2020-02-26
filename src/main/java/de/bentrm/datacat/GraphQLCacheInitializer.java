package de.bentrm.datacat;

import graphql.schema.PropertyDataFetcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GraphQLCacheInitializer implements ApplicationRunner {
    protected final Log logger = LogFactory.getLog(GraphQLCacheInitializer.class);

    public void run(ApplicationArguments args) throws Exception {
        PropertyDataFetcher.clearReflectionCache();
        logger.info("GraphQL PropertyDataFetcher cache has been cleared..");
    }
}
