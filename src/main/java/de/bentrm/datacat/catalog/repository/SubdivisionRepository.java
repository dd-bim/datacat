package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubdivisionRepository extends EntityRepository<XtdSubdivision> {

        @Query("""
                        MATCH (n {id: $subdivisionId})-[:SUBDIVISIONS]->(p:XtdSubdivision)
                        RETURN p.id""")
        List<String> findAllSubdivisionIdsAssignedToSubdivision(String subdivisionId);
}
