package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.base.domain.Entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 * Base repository used throughout the codebase.
 * Preconfigures all entities to use a string (UUID) id.
 * @param <T> The concrete entity class the repository binds to.
 */
@NoRepositoryBean
public interface EntityRepository<T extends Entity> extends Neo4jRepository<T, String> {

    @Query("""
            MATCH (o {id: $id})
            OPTIONAL MATCH (o)-[r]->(related)
            WITH o, collect(coalesce(r, [])) AS relations, collect(coalesce(related, [])) AS relatedNodes
            RETURN o, relations, relatedNodes""")
    Optional<T> findByIdWithDirectRelations(@Param("id") String id);

    @Query("""
            MATCH (o)
            WHERE o.id IN $ids
            RETURN o""")
    List<T> findAllEntitiesById(@Param("ids") List<String> ids);
}
