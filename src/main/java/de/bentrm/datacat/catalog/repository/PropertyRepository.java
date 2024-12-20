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

}
