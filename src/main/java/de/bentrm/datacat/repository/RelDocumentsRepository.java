package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import org.springframework.stereotype.Repository;

@Repository
public interface RelDocumentsRepository extends GraphEntityRepository<XtdRelDocuments> {
}
