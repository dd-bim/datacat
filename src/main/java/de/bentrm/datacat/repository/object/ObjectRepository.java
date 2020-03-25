package de.bentrm.datacat.repository.object;

import de.bentrm.datacat.domain.XtdObject;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends Neo4jRepository<XtdObject, String>, ObjectRepositoryExtension {
}
