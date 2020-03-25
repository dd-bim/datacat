package de.bentrm.datacat.repository.object;

import de.bentrm.datacat.domain.XtdUnit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends Neo4jRepository<XtdUnit, String> {
}
