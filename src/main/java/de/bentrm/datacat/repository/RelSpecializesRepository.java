package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import org.springframework.stereotype.Repository;

@Repository
public interface RelSpecializesRepository
        extends GraphEntityRepository<XtdRelSpecializes>, RelSpecializesRepositoryExtension {
}
