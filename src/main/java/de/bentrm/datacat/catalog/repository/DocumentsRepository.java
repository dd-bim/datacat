package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends EntityRepository<XtdRelDocuments> {
}
