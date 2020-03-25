package de.bentrm.datacat.repository.object;

import de.bentrm.datacat.domain.XtdActivity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends Neo4jRepository<XtdActivity, String> {
}
