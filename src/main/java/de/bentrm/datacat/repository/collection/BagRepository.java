package de.bentrm.datacat.repository.collection;

import de.bentrm.datacat.domain.collection.XtdBag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagRepository extends Neo4jRepository<XtdBag, String> {

}
