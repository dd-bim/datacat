package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdLanguage;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends EntityRepository<XtdLanguage> {

        @Query("""
            MATCH (n {id: $externalDocumentId})-[:LANGUAGES]->(p:XtdLanguage)
            RETURN p.id""")
    List<String> findAllLanguageIdsAssignedToExternalDocument(String externalDocumentId);

    @Query("""
            MATCH (n {id: $textId})-[:LANGUAGE]->(p:XtdLanguage)
            RETURN p.id""")
    String findLanguageIdAssignedToText(String textId);

    @Query("""
            MATCH(n:XtdLanguage) WHERE n.code = $languageCode 
            RETURN n""")
     XtdLanguage findByCode(String languageCode);
}
