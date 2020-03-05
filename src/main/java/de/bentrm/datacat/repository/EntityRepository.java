package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.Entity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface EntityRepository<T extends Entity> extends Neo4jRepository<T, String> {

    @Query("MATCH (n) DETACH DELETE n")
    void pruneDatabase();

}
