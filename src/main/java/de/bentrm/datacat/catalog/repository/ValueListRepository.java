package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValueListRepository extends EntityRepository<XtdValueList> {

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})-[:VALUES]->(p:XtdOrderedValue)
                        RETURN p.id""")
        List<String> findAllOrderedValueIdsAssignedToValueList(String valueListId);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})<-[:POSSIBLE_VALUES]->(p:XtdProperty)
                        RETURN p.id""")
        List<String> findAllPropertyIdsAssignedToValueList(String valueListId);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})-[:UNIT]->(p:XtdUnit)
                        RETURN p.id""")
        String findUnitIdAssignedToValueList(String valueListId);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})-[:LANGUAGE]->(p:XtdLanguage)
                        RETURN p.id""")
        String findLanguageIdAssignedToValueList(String valueListId);

}
