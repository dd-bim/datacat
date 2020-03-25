package de.bentrm.datacat.repository.collection;

import de.bentrm.datacat.domain.collection.XtdNest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NestRepository extends Neo4jRepository<XtdNest, String> {
}
