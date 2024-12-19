package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends EntityRepository<XtdSubject> {

        @Query("""
                        MATCH (n {id: $propertyId})<-[:PROPERTIES]-(p:XtdSubject)
                        RETURN p.id""")
        List<String> findAllSubjectIdsAssignedToProperty(String propertyId);

        @Query("""
                        MATCH (n {id: $relationshiptToSubjectId})-[:SCOPE_SUBJECTS]->(p:XtdSubject)
                        RETURN p.id""")
        List<String> findAllScopeSubjectIdsAssignedToRelationshipToSubject(String relationshiptToSubjectId);

}
