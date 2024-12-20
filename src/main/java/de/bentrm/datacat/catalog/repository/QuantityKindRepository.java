package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityKindRepository extends EntityRepository<XtdQuantityKind> {

    @Query("""
            MATCH (n:XtdQuantityKind {id: $quantityKindId})-[:UNITS]->(p:XtdUnit)
            RETURN p.id""")
    List<String> findAllUnitIdsAssignedToQuantityKind(String quantityKindId);

    @Query("""
            MATCH (n:XtdQuantityKind {id: $quantityKindId})-[:DIMENSION]->(p:XtdDimension)
            RETURN p.id""")
    String findDimensionIdAssignedToQuantityKind(String quantityKindId);

}
