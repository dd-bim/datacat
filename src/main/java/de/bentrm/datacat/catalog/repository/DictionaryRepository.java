package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdDictionary;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends EntityRepository<XtdDictionary> {

        @Query("""
                        MATCH (n:XtdDictionary {id: $dictionaryId})-[:NAME]->(p:XtdMultiLanguageText)
                        RETURN p.id""")
        String findMultiLanguageTextIdAssignedToDictionary(String dictionaryId);

}
