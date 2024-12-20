package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends EntityRepository<XtdSubject> {

        @Query("""
                        MATCH (n:XtdProperty {id: $propertyId})<-[:PROPERTIES]-(p:XtdSubject)
                        RETURN p.id""")
        List<String> findAllSubjectIdsAssignedToProperty(String propertyId);

        @Query("""
                        MATCH (n:XtdRelationshipToSubject {id: $relationshiptToSubjectId})-[:SCOPE_SUBJECTS]->(p:XtdSubject)
                        RETURN p.id""")
        List<String> findAllScopeSubjectIdsAssignedToRelationshipToSubject(String relationshiptToSubjectId);

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
}
