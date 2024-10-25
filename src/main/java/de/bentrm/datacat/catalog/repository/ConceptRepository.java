package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptRepository extends EntityRepository<XtdConcept> {

    @Query("""
            MATCH (n {id: $externalDocumentId})<-[:REFERENCE_DOCUMENTS]-(p:XtdConcept)
            RETURN p.id""")
    List<String> findAllConceptIdsAssignedToExternalDocument(String externalDocumentId);


    @Query("""
            MATCH (n {id: $conceptId})-[:SIMILAR_TO]->(p:XtdConcept)
            RETURN p.id""")
    List<String> findAllConceptIdsAssignedToConcept(String conceptId);
}
