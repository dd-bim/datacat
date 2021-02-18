package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.base.domain.Entity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository used throughout the codebase.
 * Preconfigures all entities to use a string (UUID) id.
 * @param <T> The concrete entity class the repository binds to.
 */
@NoRepositoryBean
public interface EntityRepository<T extends Entity> extends Neo4jRepository<T, String> {

}
