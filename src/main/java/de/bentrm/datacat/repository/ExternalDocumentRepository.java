package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdExternalDocument;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalDocumentRepository extends Neo4jRepository<XtdExternalDocument, String> {}
