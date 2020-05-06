package de.bentrm.datacat;

import de.bentrm.datacat.repository.impl.GraphEntityRepositoryFactoryBean;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.event.EventListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableConfigurationProperties({Neo4jProperties.class})
@EnableNeo4jRepositories(
        basePackages = {"de.bentrm.datacat.repository"},
        repositoryFactoryBeanClass = GraphEntityRepositoryFactoryBean.class
)
@EnableTransactionManagement
public class DataStoreConfiguration {

    @Bean
    public org.neo4j.ogm.config.Configuration configuration(Neo4jProperties properties) {
        return properties.createConfiguration();
    }

    @Bean
    SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration, BeanFactory beanFactory, ObjectProvider<EventListener> eventListeners) {
        SessionFactory sessionFactory = new SessionFactory(
                configuration,
                "de.bentrm.datacat.domain",
                "de.bentrm.datacat.domain.collection",
                "de.bentrm.datacat.domain.relationship");
        eventListeners.orderedStream().forEach(sessionFactory::register);
        return sessionFactory;
    }

    @Bean
    public Neo4jTransactionManager transactionManager(SessionFactory sessionFactory, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        Neo4jTransactionManager transactionManager = new Neo4jTransactionManager(sessionFactory);
        transactionManagerCustomizers.ifAvailable((customizers) -> {
            customizers.customize(transactionManager);
        });
        return transactionManager;
    }

}
