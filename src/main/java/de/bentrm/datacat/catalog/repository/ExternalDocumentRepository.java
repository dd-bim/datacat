package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalDocumentRepository extends EntityRepository<XtdExternalDocument> {

        @Query("""
            MATCH (n {id: $conceptId})-[:REFERENCE_DOCUMENTS]->(p:XtdExternalDocument)
            RETURN p.id""")
    List<String> findAllExternalDocumentIdsAssignedToConcept(String conceptId);
}
