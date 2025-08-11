package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdInterval;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalRepository extends EntityRepository<XtdInterval> {

    @Query("""
            MATCH (n:XtdInterval {id: $intervalId})-[:MAXIMUM]->(p:XtdValueList)
            RETURN p.id""")
    String findMaxValueListIdAssignedToInterval(String intervalId);

    @Query("""
            MATCH (n:XtdInterval {id: $intervalId})-[:MINIMUM]->(p:XtdValueList)
            RETURN p.id""")
    String findMinValueListIdAssignedToInterval(String intervalId);

}
