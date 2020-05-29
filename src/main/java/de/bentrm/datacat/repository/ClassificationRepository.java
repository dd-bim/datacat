package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdClassification;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificationRepository extends GraphEntityRepository<XtdClassification> { }
