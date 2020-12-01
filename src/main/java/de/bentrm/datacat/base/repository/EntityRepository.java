package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.base.domain.Entity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository<T extends Entity> extends Neo4jRepository<T, String> {

}
