package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import org.springframework.stereotype.Repository;

@Repository
public interface RelCollectsRepository extends GraphEntityRepository<XtdRelCollects, String> {}
