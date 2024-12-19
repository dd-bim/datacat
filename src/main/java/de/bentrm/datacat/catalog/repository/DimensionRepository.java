package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionRepository extends EntityRepository<XtdDimension> {

    @Query("""
            MATCH (n {id: $propertyId})-[:DIMENSION]->(p:XtdDimension)
            RETURN p.id""")
    String findDimensionIdAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n {id: $quantityKindId})-[:DIMENSION]->(p:XtdDimension)
            RETURN p.id""")
    String findDimensionIdAssignedToQuantityKind(String quantityKindId);
}
