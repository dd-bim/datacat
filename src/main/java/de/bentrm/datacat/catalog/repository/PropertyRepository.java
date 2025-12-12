package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends EntityRepository<XtdProperty> {

    @Query("""
            MATCH (p:XtdProperty {id: $id})
            RETURN p""")
    Optional<XtdProperty> findByIdWithoutRelations(String id);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})<-[:PROPERTIES]-(p:XtdSubject)
            RETURN p.id""")
    List<String> findAllSubjectIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})-[:POSSIBLE_VALUES]->(p:XtdValueList)
            RETURN p.id""")
    List<String> findAllValueListIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})-[:UNITS]->(p:XtdUnit)
            RETURN p.id""")
    List<String> findAllUnitIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})-[:CONNECTED_PROPERTIES]->(p:XtdRelationshipToProperty)
            RETURN p.id""")
    List<String> findAllConnectedPropertyRelationshipIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})<-[:TARGET_PROPERTIES]-(p:XtdRelationshipToProperty)
            RETURN p.id""")
    List<String> findAllConnectingPropertyRelationshipIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})-[:DIMENSION]->(p:XtdDimension)
            RETURN p.id""")
    String findDimensionIdAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})-[:SYMBOLS]->(p:XtdSymbol)
            RETURN p.id""")
    List<String> findAllSymbolIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (n:XtdProperty {id: $propertyId})-[:BOUNDARY_VALUES]->(p:XtdInterval)
            RETURN p.id""")
    List<String> findAllIntervalIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (:XtdProperty {id: $propertyId})-[:QUANTITY_KINDS]->(p:XtdQuantityKind)
            RETURN p.id""")
    List<String> findAllQuantityKindIdsAssignedToProperty(String propertyId);

    @Query("""
            MATCH (p:XtdProperty {id: $id})
            OPTIONAL MATCH (p)-[r1]->(related1)
            OPTIONAL MATCH (p)<-[r2]-(related2)
            
            // Explizit RelationshipToProperty Relationen laden
            OPTIONAL MATCH (p)-[:CONNECTED_PROPERTIES]->(rel1:XtdRelationshipToProperty)
            OPTIONAL MATCH (rel1)-[rel1R]->(rel1Related)
            OPTIONAL MATCH (p)<-[:TARGET_PROPERTIES]-(rel2:XtdRelationshipToProperty)
            OPTIONAL MATCH (rel2)-[rel2R]->(rel2Related)
            
            WITH p, 
                 collect(coalesce(r1, [])) + collect(coalesce(r2, [])) + collect(coalesce(rel1R, [])) + collect(coalesce(rel2R, [])) AS relations, 
                 collect(coalesce(related1, [])) + collect(coalesce(related2, [])) + collect(coalesce(rel1Related, [])) + collect(coalesce(rel2Related, [])) AS relatedNodes
            RETURN p, relations, relatedNodes
            """)
    Optional<XtdProperty> findByIdWithIncomingAndOutgoingRelations(String id);

}
