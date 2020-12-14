package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends EntityRepository<XtdRelationship> {

    @Query("MATCH (n:CatalogItem)-->(r:XtdRelationship) WHERE n.id = $id RETURN r.id")
    List<String> findAllRelationshipsByRelatingId(String id);

}
