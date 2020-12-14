package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends EntityRepository<XtdProperty> {

    @Query("""
            MATCH (n {id: $subjectId})-[:ASSIGNS_COLLECTIONS|COLLECTS*]->(p:XtdProperty)
            RETURN p.id
            UNION
            MATCH (n {id: $subjectId})-[:ASSIGNS_PROPERTY*2]->(p:XtdProperty)
            RETURN p.id""")
    List<String> findAllPropertyIdsAssignedToSubject(String subjectId);

}
