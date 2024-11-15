package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityKindRepository extends EntityRepository<XtdQuantityKind> {

        @Query("""
            MATCH (n {id: $propertyId})-[:QUANTITY_KINDS]->(p:XtdQuantityKind)
            RETURN p.id""")
    List<String> findAllQuantityKindIdsAssignedToProperty(String propertyId);

}
