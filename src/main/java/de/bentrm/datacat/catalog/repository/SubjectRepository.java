package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends EntityRepository<XtdSubject> {

        @Query("""
                        MATCH (s:XtdSubject {id: $id})
                        RETURN s""")
        Optional<XtdSubject> findByIdWithoutRelations(String id);

        @Query("""
                        MATCH (n:XtdSubject {id: $subjectId})-[:PROPERTIES]->(p:XtdProperty)
                        RETURN p.id""")
        List<String> findAllPropertyIdsAssignedToSubject(String subjectId);

        @Query("""
                        MATCH (n:XtdSubject {id: $subjectId})-[:CONNECTED_SUBJECTS]->(p:XtdRelationshipToSubject)
                        RETURN p.id""")
        List<String> findAllConnectedSubjectRelationshipIdsAssignedToSubject(String subjectId);

        @Query("""
                        MATCH (n:XtdSubject {id: $subjectId})<-[:TARGET_SUBJECTS]-(p:XtdRelationshipToSubject)
                        RETURN p.id""")
        List<String> findAllConnectingSubjectRelationshipIdsAssignedToSubject(String subjectId);

        @Query("""
                        MATCH (s:XtdSubject {id: $id})
                        OPTIONAL MATCH (s)-[r1]->(related1)
                        OPTIONAL MATCH (s)<-[r2]-(related2)
                        
                        // Explizit RelationshipToSubject Relationen laden
                        OPTIONAL MATCH (s)-[:CONNECTED_SUBJECTS]->(rel1:XtdRelationshipToSubject)
                        OPTIONAL MATCH (rel1)-[rel1R]->(rel1Related)
                        OPTIONAL MATCH (s)<-[:TARGET_SUBJECTS]-(rel2:XtdRelationshipToSubject)
                        OPTIONAL MATCH (rel2)-[rel2R]->(rel2Related)
                        
                        WITH s, 
                             collect(coalesce(r1, [])) + collect(coalesce(r2, [])) + collect(coalesce(rel1R, [])) + collect(coalesce(rel2R, [])) AS relations, 
                             collect(coalesce(related1, [])) + collect(coalesce(related2, [])) + collect(coalesce(rel1Related, [])) + collect(coalesce(rel2Related, [])) AS relatedNodes
                        RETURN s, relations, relatedNodes
                        """)
        Optional<XtdSubject> findByIdWithIncomingAndOutgoingRelations(String id);
}
