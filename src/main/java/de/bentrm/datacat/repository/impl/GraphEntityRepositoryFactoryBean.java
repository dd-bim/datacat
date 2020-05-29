package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.repository.GraphEntityRepository;
import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.neo4j.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.mapping.Neo4jPersistentEntity;
import org.springframework.data.neo4j.mapping.Neo4jPersistentProperty;
import org.springframework.data.neo4j.repository.support.Neo4jRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.util.Assert;

public class GraphEntityRepositoryFactoryBean<R extends GraphEntityRepository<T>, T>
        extends TransactionalRepositoryFactoryBeanSupport<R, T, String> {

    private static final Logger logger = LoggerFactory.getLogger(GraphEntityRepositoryFactoryBean.class);

    private Session session;
    private Neo4jMappingContext mappingContext;

    public GraphEntityRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
        logger.debug("New GraphEntityRepositoryFactoryBean for interface {} initialized.", repositoryInterface);
    }

    @Autowired
    public void setSession(Session session) {
        this.session = session;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(org.springframework.data.mapping.context.MappingContext)
     */
    @Override
    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);

        if (mappingContext instanceof Neo4jMappingContext) {
            this.mappingContext = (Neo4jMappingContext) mappingContext;
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(session, "Session must not be null!");
        super.afterPropertiesSet();
    }

    @NotNull
    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        return new GraphEntityRepositoryFactory(session, mappingContext);
    }

    private static class GraphEntityRepositoryFactory extends Neo4jRepositoryFactory {

        public GraphEntityRepositoryFactory(Session session, MappingContext<Neo4jPersistentEntity<?>, Neo4jPersistentProperty> mappingContext) {
            super(session, mappingContext);
            logger.debug("New GraphEntityRepositoryFactory initialized from session {} and mapping context {}", session, mappingContext);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata repositoryMetadata) {
            return GraphEntityRepositoryBaseClass.class;
        }


    }
}
