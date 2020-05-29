package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import org.springframework.stereotype.Repository;

@Repository
public interface RelAssociatesRepository
        extends GraphEntityRepository<XtdRelAssociates>, RelAssociatesRepositoryExtension {
}
