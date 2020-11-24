package de.bentrm.datacat;

import de.bentrm.datacat.base.repository.GraphEntityRepositoryFactoryBean;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.config.Configuration;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataNeo4jTest
@EnableNeo4jRepositories(
        basePackages = {
                "de.bentrm.datacat.base.repository",
                "de.bentrm.datacat.auth.repository",
                "de.bentrm.datacat.catalog.repository"
        },
        repositoryFactoryBeanClass = GraphEntityRepositoryFactoryBean.class
)
public class Neo4jTest {

    @Container
    private static final Neo4jContainer databaseServer = new Neo4jContainer()
            .withoutAuthentication();

    @TestConfiguration
    static class Config {

        @Bean
        public org.neo4j.ogm.config.Configuration configuration() {
            return new Configuration.Builder()
                    .uri(databaseServer.getBoltUrl())
                    .credentials("neo4j", databaseServer.getAdminPassword())
                    .build();
        }
    }


    @Test
    public void canStart() {}

}
