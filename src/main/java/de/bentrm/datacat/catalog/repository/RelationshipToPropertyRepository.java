package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipToPropertyRepository extends EntityRepository<XtdRelationshipToProperty> {

    @Query("""
            MATCH (n {id: $propertyId})-[:CONNECTED_PROPERTIES]->(p:XtdRelationshipToProperty)
            RETURN p.id""")
    List<String> findAllConnectedPropertyRelationshipIdsAssignedToProperty(String propertyId);

    @Query("""
        MATCH (n {id: $propertyId})<-[:TARGET_PROPERTIES]-(p:XtdRelationshipToProperty)
        RETURN p.id""")
    List<String> findAllConnectingPropertyRelationshipIdsAssignedToProperty(String propertyId);
}
