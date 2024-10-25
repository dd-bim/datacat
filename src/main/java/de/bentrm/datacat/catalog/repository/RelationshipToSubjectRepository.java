package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToSubject;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipToSubjectRepository extends EntityRepository<XtdRelationshipToSubject> {

    @Query("""
            MATCH (n {id: $subjectId})-[:CONNECTED_SUBJECTS]->(p:XtdRelationshipToSubject)
            RETURN p.id""")
    List<String> findAllConnectedSubjectRelationshipIdsAssignedToSubject(String subjectId);

    @Query("""
        MATCH (n {id: $subjectId})<-[:TARGET_SUBJECTS]-(p:XtdRelationshipToSubject)
        RETURN p.id""")
    List<String> findAllConnectingSubjectRelationshipIdsAssignedToSubject(String subjectId);
}
