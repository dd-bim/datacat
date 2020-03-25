package de.bentrm.datacat.repository.object;

import de.bentrm.datacat.domain.XtdActor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends Neo4jRepository<XtdActor, String> {
}
