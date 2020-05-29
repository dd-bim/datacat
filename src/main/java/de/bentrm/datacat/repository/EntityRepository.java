package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends GraphEntityRepository<XtdEntity>, EntityRepositoryExtension {
}
