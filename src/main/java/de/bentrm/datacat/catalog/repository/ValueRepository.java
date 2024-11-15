package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdValue;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueRepository extends EntityRepository<XtdValue> {

    @Query("""
            MATCH (n {id: $orderedValueId})-[:ORDERED_VALUE]->(p:XtdValue)
            RETURN p.id""")
    String findValueIdAssignedToOrderedValue(String orderedValueId);
}
