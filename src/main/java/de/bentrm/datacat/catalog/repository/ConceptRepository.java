package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptRepository extends EntityRepository<XtdConcept> {

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:SIMILAR_TO]->(p:XtdConcept)
            RETURN p.id""")
    List<String> findAllConceptIdsAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:REFERENCE_DOCUMENTS]->(p:XtdExternalDocument)
            RETURN p.id""")
    List<String> findAllExternalDocumentIdsAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:EXAMPLES]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    List<String> findAllExampleIdsAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:COUNTRY_OF_ORIGIN]->(p:XtdCountry)
            RETURN p.id""")
    String findCountryIdAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:DEFINITION]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    String findDefinitionIdAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:LANGUAGE_OF_CREATOR]->(p:XtdLanguage)
            RETURN p.id""")
    String findLanguageIdAssignedToConcept(String conceptId);

    @Query("""
            MATCH (n:XtdConcept {id: $conceptId})-[:DESCRIPTIONS]->(p:XtdMultiLanguageText)
            RETURN p.id""")
    List<String> findAllDescriptionIdsAssignedToConcept(String conceptId);
}
