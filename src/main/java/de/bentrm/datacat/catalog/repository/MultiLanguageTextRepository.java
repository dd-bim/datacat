package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiLanguageTextRepository extends EntityRepository<XtdMultiLanguageText> {

    @Query("""
            MATCH (n {id: $conceptId})-[:EXAMPLES]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    List<String> findAllMultiLanguageTextIdsAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n {id: $dictionaryId})-[:NAME]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    String findMultiLanguageTextIdAssignedToDictionary(String dictionaryId);

    @Query("""
            MATCH (n {id: $objectId})-[:DEPRECATION_EXPLANATION]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    String findMultiLanguageTextIdAssignedToObject(String objectId);

    @Query("""
            MATCH (n {id: $objectId})-[:NAMES]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    List<String> findAllNamesAssignedToObject(String objectId);
}
