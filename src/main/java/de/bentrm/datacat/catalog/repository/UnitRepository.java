package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdUnit;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends EntityRepository<XtdUnit> {

        @Query("""
                MATCH (n:XtdUnit {id: $unitId})<-[:UNITS]-(p:XtdProperty)
                RETURN p.id""")
        List<String> findAllPropertyIdsAssigningUnit(String unitId);

        @Query("""
                MATCH (n:XtdUnit {id: $unitId})-[:DIMENSION]->(p:XtdDimension)
                RETURN p.id""")
        String findDimensionIdAssignedToUnit(String unitId);
    
        @Query("""
                MATCH (n:XtdUnit {id: $unitId})-[:SYMBOL]->(p:XtdMultiLanguageText)
                RETURN p.id""")
        String findSymbolIdAssignedToUnit(String unitId);

        @Query("""
                MATCH (n:XtdUnit {id: $unitId})-[:COEFFICIENT]->(p:XtdRational)
                RETURN p.id""")
        String findCoefficientIdAssignedToUnit(String unitId);

        @Query("""
                MATCH (n:XtdUnit {id: $unitId})-[:OFFSET]->(p:XtdRational)
                RETURN p.id""")
        String findOffsetIdAssignedToUnit(String unitId);

        @Query("""
                MATCH (n:XtdUnit {id: $unitId})<-[:UNIT]-(v:XtdValueList)
                RETURN v.id
                        """)
        List<String> findValueListIdsAssigningUnit(String unitId);
}
