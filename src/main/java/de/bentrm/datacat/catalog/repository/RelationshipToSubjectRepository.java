package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipToSubjectRepository extends EntityRepository<XtdRelationshipToSubject> {

    @Query("""
            MATCH (n:XtdRelationshipToSubject {id: $relationshiptToSubjectId})-[:SCOPE_SUBJECTS]->(p:XtdSubject)
            RETURN p.id""")
    List<String> findAllScopeSubjectIdsAssignedToRelationshipToSubject(String relationshiptToSubjectId);

    @Query("""
            MATCH (n:XtdRelationshipToSubject {id: $relationshiptToSubjectId})-[:TARGET_SUBJECTS]->(p:XtdSubject)
            RETURN p.id""")
    List<String> findAllTargetSubjectIdsAssignedToRelationshipToSubject(String relationshiptToSubjectId);

    @Query("""
            MATCH (n:XtdRelationshipToSubject {id: $relationshiptToSubjectId})<-[:CONNECTED_SUBJECTS]-(p:XtdSubject)
            RETURN p.id LIMIT 1""")
    String findConnectingSubjectIdAssignedToRelationshipToSubject(String relationshiptToSubjectId);

    @Query("""
            MATCH (n:XtdRelationshipToSubject {id: $relationshiptToSubjectId})-[:RELATIONSHIP_TYPE]->(p:XtdRelationshipType)
            RETURN p.id""")
    String findRelationshipTypeIdAssignedToRelationshipToSubject(String relationshiptToSubjectId);

    @Query("""
            MATCH (n:XtdRelationshipToSubject)-[:RELATIONSHIP_TYPE]->(p:XtdRelationshipType {id: $relationshipTypeId})
            RETURN count(n)""")
    Long countRelationshipsUsingRelationshipType(String relationshipTypeId);
}
