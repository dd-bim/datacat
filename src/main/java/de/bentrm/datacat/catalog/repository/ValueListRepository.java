package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueListRepository extends EntityRepository<XtdValueList> {

        @Query("""
                        MATCH (n {id: $propertyId})-[:POSSIBLE_VALUES]->(p:XtdValueList)
                        RETURN p.id""")
        List<String> findAllValueListIdsAssignedToProperty(String propertyId);

        @Query("""
                        MATCH (n {id: $intervalId})-[:MAXIMUM]->(p:XtdValueList)
                        RETURN p.id""")
        String findMaxValueListIdAssignedToInterval(String intervalId);

        @Query("""
                        MATCH (n {id: $intervalId})-[:MINIMUM]->(p:XtdValueList)
                        RETURN p.id""")
        String findMinValueListIdAssignedToInterval(String intervalId);

        @Query("""
                MATCH (n:XtdValueList {id: $valueListId})-[:VALUES]->(p:XtdOrderedValue)
                RETURN p.id""")
        List<String> findAllOrderedValueIdsAssignedToValueList(String valueListId);

        @Query("""
                MATCH (n:XtdValueList {id: $valueListId})<-[:POSSIBLE_VALUES]->(p:XtdProperty)
                RETURN p.id""")
        List<String> findAllPropertyIdsAssignedToValueList(String valueListId);
    
        @Query("""
                MATCH (n {id: $valueListId})-[:UNIT]->(p:XtdUnit)
                RETURN p.id""")
        String findUnitIdAssignedToValueList(String valueListId);

        @Query("""
                MATCH (n {id: $valueListId})-[:LANGUAGE]->(p:XtdLanguage)
                RETURN p.id""")
        String findLanguageIdAssignedToValueList(String valueListId);
    
}
