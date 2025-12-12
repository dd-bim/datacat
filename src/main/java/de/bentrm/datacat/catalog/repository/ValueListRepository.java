package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValueListRepository extends EntityRepository<XtdValueList> {

        @Query("""
                        MATCH (vl:XtdValueList {id: $id})
                        RETURN vl""")
        Optional<XtdValueList> findByIdWithoutRelations(String id);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})-[:VALUES]->(p:XtdOrderedValue)
                        RETURN p.id""")
        List<String> findAllOrderedValueIdsAssignedToValueList(String valueListId);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})-[:VALUES]->(p:XtdOrderedValue)
                        RETURN p.id
                        ORDER BY p.orderIndex
                        SKIP $skip LIMIT $limit""")
        List<String> findOrderedValuesByValueListIdPaginated(String valueListId, int skip, int limit);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})-[:VALUES]->(p:XtdOrderedValue)
                        RETURN count(p)""")
        Long countOrderedValuesByValueListId(String valueListId);

        @Query("""
                        MATCH (n:XtdValueList {id: $valueListId})<-[:POSSIBLE_VALUES]-(p:XtdProperty)
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

        @Query("""
                        MATCH (vl:XtdValueList {id: $id})
                        OPTIONAL MATCH (vl)-[r]->(related)
                        WITH vl, collect(coalesce(r, [])) AS relations, collect(coalesce(related, [])) AS relatedNodes
                        RETURN vl, relations, relatedNodes
                        """)
        Optional<XtdValueList> findByIdWithAllRelations(String id);

        @Query("""
                        MATCH (vl:XtdValueList {id: $id})
                        OPTIONAL MATCH (vl)-[r1]->(related1)
                        OPTIONAL MATCH (vl)<-[r2]-(related2)
                        
                        // Explizit OrderedValues und deren Values laden
                        OPTIONAL MATCH (vl)-[:VALUES]->(ov:XtdOrderedValue)
                        OPTIONAL MATCH (ov)-[ovRel]->(ovRelated)
                        OPTIONAL MATCH (ov)-[:ORDERED_VALUE]->(v:XtdValue)
                        OPTIONAL MATCH (v)-[vRel]->(vRelated)
                        
                        WITH vl, 
                             collect(coalesce(r1, [])) + collect(coalesce(r2, [])) + collect(coalesce(ovRel, [])) + collect(coalesce(vRel, [])) AS relations, 
                             collect(coalesce(related1, [])) + collect(coalesce(related2, [])) + collect(coalesce(ovRelated, [])) + collect(coalesce(vRelated, [])) AS relatedNodes
                        RETURN vl, relations, relatedNodes
                        """)
        Optional<XtdValueList> findByIdWithIncomingAndOutgoingRelations(String id);

}
