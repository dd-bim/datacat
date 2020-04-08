package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdProperty;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends GraphEntityRepository<XtdProperty, String> {
}
