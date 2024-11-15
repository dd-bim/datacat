package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends EntityRepository<AbstractRelationship> {

    /**
     * Given a catalog record id, this methods returns all relationships that
     * this record instantiates, i.e. the record is on the relating side of the relationship.
     * @param id of a catalog record.
     * @return Ids of all relationships that this record instantiates.
     */
    @Query("""
        MATCH (n:CatalogRecord)-->(r:Relationship) 
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
           MATCH (r:Relationship)-->(n:CatalogRecord)
           WHERE n.id = $id
           WITH r
           MATCH (r)-->(m:CatalogRecord)
           WITH r, count(m) as num
           WHERE num < 2
           RETURN r.id
    """)
    List<String> findAllSingularRelationshipsByRelatedId(String id);

    /**
     * Removes a relationship between two catalog records.
     * @param recordId of the first catalog record.
     * @param relatedRecordId of the second catalog record.
     * @param relationType of the relationship.
     */
    @Query("""
        MATCH (n:CatalogRecord) - [x] -> (m:CatalogRecord) 
        WHERE n.id = $recordId AND m.id = $relatedRecordId AND type(x) = $relationType DELETE x
    """)
    void removeRelationship(String recordId, String relatedRecordId, String relationType);
}
