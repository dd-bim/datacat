package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.GraphEntityRepository;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalDocumentRepository extends GraphEntityRepository<XtdExternalDocument> {}
