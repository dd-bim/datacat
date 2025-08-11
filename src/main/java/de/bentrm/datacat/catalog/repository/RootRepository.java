package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RootRepository extends EntityRepository<XtdRoot> {

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
