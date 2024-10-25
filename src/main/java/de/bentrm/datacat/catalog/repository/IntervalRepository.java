package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdInterval;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalRepository extends EntityRepository<XtdInterval> {

        @Query("""
            MATCH (n {id: $propertyId})-[:BOUNDARY_VALUES]->(p:XtdInterval)
            RETURN p.id""")
    List<String> findAllIntervalIdsAssignedToProperty(String propertyId);

}
