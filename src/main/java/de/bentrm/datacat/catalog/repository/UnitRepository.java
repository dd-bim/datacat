package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdUnit;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends EntityRepository<XtdUnit> {

    @Query("""
            MATCH (n {id: $propertyId})-[:UNITS]->(p:XtdUnit)
            RETURN p.id""")
    List<String> findAllUnitIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n {id: $quantityKindId})-[:UNITS]->(p:XtdUnit)
            RETURN p.id""")
    List<String> findAllUnitIdsAssignedToQuantityKind(String quantityKindId);

    @Query("""
            MATCH (n {id: $valueListId})-[:UNIT]->(p:XtdUnit)
            RETURN p.id""")
    String findUnitIdAssignedToValueList(String valueListId);
}
