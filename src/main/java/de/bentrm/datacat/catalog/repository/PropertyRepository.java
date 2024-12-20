package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends EntityRepository<XtdProperty> {

    @Query("""
            MATCH (n:XtdUnit {id: $unitId})<-[:UNITS]->(p:XtdProperty)
            RETURN p.id""")
    List<String> findAllPropertyIdsAssignedToUnit(String unitId);
}
