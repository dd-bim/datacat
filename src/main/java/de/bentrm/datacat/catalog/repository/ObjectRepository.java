package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdObject;
import org.springframework.stereotype.Repository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

@Repository
public interface ObjectRepository extends EntityRepository<XtdObject> {

        @Query("""
                        MATCH (n:XtdObject {id: $objectId})-[:REPLACED_OBJECTS]->(p:XtdObject)
                        RETURN p.id""")
        List<String> findAllReplacedObjectIdsAssignedToObject(String objectId);

        @Query("""
                        MATCH (n:XtdObject {id: $objectId})<-[:REPLACED_OBJECTS]-(p:XtdObject)
                        RETURN p.id""")
        List<String> findAllReplacingObjectIdsAssignedToObject(String objectId);

        @Query("""
                        MATCH (n:XtdObject {id: $objectId})-[:NAMES]->(p:XtdMultiLanguageText)
                        RETURN p.id""")
        List<String> findAllNamesAssignedToObject(String objectId);

        @Query("""
                        MATCH (n:XtdObject {id: $objectId})-[:DICTIONARY]->(p:XtdDictionary)
                        RETURN p.id""")
        String findDictionaryIdAssignedToObject(String objectId);

        @Query("""
                        MATCH (n:XtdObject {id: $objectId})-[:DEPRECATION_EXPLANATION]->(p:XtdMultiLanguageText)
                        RETURN p.id""")
        String findMultiLanguageTextIdAssignedToObject(String objectId);

        @Query("""
                        MATCH (n:XtdObject {id: $objectId})-[:COMMENTS]->(p:XtdMultiLanguageText)
                        RETURN p.id""")
        List<String> findCommentsAssignedToObject(String objectId);

        @Query("""
                        MATCH(x:XtdObject {id: $fromId})-[]->(y:XtdRelationshipToSubject|XtdRelationshipToProperty)-[]->(z:XtdObject {id: $toId}) 
                        RETURN y.id""")
        String findRelationshipBetweenObjects(String fromId, String toId);

        @Query("""
                        MATCH(x:XtdObject {id: $fromId})-[]->(y:XtdRelationshipToSubject|XtdRelationshipToProperty)-[]->(z:XtdObject {id: $toId}) 
                        RETURN y.id""")
        List<String> findAllRelationshipsBetweenObjects(String fromId, String toId);

        @Query("""
                        MATCH(x:XtdObject {id: $objectId})-[n:TARGET_PROPERTIES|TARGET_SUBJECTS]->() 
                        RETURN count(n)
                        """)
        Long countTargetRelationships(String objectId);
}
