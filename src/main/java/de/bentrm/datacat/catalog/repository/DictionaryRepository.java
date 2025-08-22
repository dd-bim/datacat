package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdDictionary;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends EntityRepository<XtdDictionary> {

        @Query("""
                        MATCH (n:XtdDictionary {id: $dictionaryId})-[:NAME]->(p:XtdMultiLanguageText)
                        RETURN p.id""")
        String findMultiLanguageTextIdAssignedToDictionary(String dictionaryId);

        @Query("""
                        MATCH (n:XtdDictionary {id: $dictionaryId})<-[:DICTIONARY]-(p:XtdObject)
                        RETURN p.id""")
        List<String> findConceptsByDictionaryId(String dictionaryId);

        @Query("""
                        MATCH (n:XtdDictionary {id: $dictionaryId})<-[:DICTIONARY]-(p:XtdObject)
                        RETURN p.id
                        ORDER BY p.`labels.de`
                        SKIP $skip LIMIT $limit""")
        List<String> findConceptsByDictionaryIdPaginated(String dictionaryId, int skip, int limit);

        @Query("""
                        MATCH (n:XtdDictionary {id: $dictionaryId})<-[:DICTIONARY]-(p:XtdObject)
                        RETURN count(p)""")
        Long countConceptsByDictionaryId(String dictionaryId);
}
