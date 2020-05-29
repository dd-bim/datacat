package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdExternalDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalDocumentRepository extends GraphEntityRepository<XtdExternalDocument> {}
