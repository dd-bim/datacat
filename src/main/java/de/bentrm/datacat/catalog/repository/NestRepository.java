package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdNest;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NestRepository extends EntityRepository<XtdNest> {

    @Query("""
       MATCH (s:XtdSubject)-->(:XtdRelAssignsCollections)-->(n:XtdNest)
       WHERE s.id=$subjectId
       AND (n)-[:TAGGED]->(:Tag {id: "a27c8e3c-5fd1-47c9-806a-6ded070efae8"})
       RETURN n.id
    """)
    List<String> findAllGroupOfPropertiesIdsAssignedToSubject(String subjectId);
}
