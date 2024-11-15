package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdDictionary;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends EntityRepository<XtdDictionary> {

        @Query("""
                        MATCH (n {id: $objectId})-[:DICTIONARY]->(p:XtdDictionary)
                        RETURN p.id""")
        String findDictionaryIdAssignedToObject(String objectId);

}
