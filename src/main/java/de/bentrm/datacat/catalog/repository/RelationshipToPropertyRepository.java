package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipToPropertyRepository extends EntityRepository<XtdRelationshipToProperty> {

    @Query("MATCH (r:XtdRelationshipToProperty {id: $relationshipToPropertyId})<-[:CONNECTED_PROPERTIES]-(p:XtdProperty) RETURN p.id")
    String findConnectingPropertyId(String relationshipToPropertyId);

    @Query("MATCH (r:XtdRelationshipToProperty {id: $relationshipToPropertyId})-[:TARGET_PROPERTIES]->(p:XtdProperty) RETURN p.id")
    List<String> findTargetPropertyIds(String relationshipToPropertyId);

}
