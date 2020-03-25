package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdRoot;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RootRepository extends Neo4jRepository<XtdRoot, String> {
}
