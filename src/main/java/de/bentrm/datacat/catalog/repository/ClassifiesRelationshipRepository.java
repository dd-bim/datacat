package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.GraphEntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRelClassifies;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifiesRelationshipRepository extends GraphEntityRepository<XtdRelClassifies> {
}
