package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdObject;
import org.springframework.stereotype.Repository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

@Repository
public interface ObjectRepository extends EntityRepository<XtdObject> {

    @Query("""
            MATCH (n {id: $objectId})-[:REPLACED_OBJECTS]->(p:XtdObject)
            RETURN p.id""")
    List<String> findAllReplacedObjectIdsAssignedToObject(String objectId);

    @Query("""
            MATCH (n {id: $objectId})<-[:REPLACED_OBJECTS]-(p:XtdObject)
            RETURN p.id""")
    List<String> findAllReplacingObjectIdsAssignedToObject(String objectId);
}
