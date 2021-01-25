package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends EntityRepository<XtdRelationship> {

    /**
     * Given a catalog record id, this methods returns all relationships that
     * this record instantiates, i.e. the record is on the relating side of the relationship.
     * @param id of a catalog record.
     * @return Ids of all relationships that this record instantiates.
     */
    @Query("""
        MATCH (n:CatalogItem)-->(r:XtdRelationship) 
        WHERE n.id = $id RETURN r.id
    """)
    List<String> findAllRelationshipsByRelatingId(String id);

    /**
     * Given a catalog record id, this methods returns all relationships that
     * only relate to this record, i.e. this relationship will be empty after
     * deleting the given record.
     * @param id of a catalog record.
     * @return Ids of all singular relationships that only relate to the given id.
     */
    @Query("""
           MATCH (r:XtdRelationship)-->(n:CatalogItem)
           WHERE n.id = $id
           WITH r
           MATCH (r)-->(m:CatalogItem)
           WITH r, count(m) as num
           WHERE num < 2
           RETURN r.id
    """)
    List<String> findAllSingularRelationshipsByRelatedId(String id);

}
