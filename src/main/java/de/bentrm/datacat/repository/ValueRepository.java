package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdValue;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueRepository extends GraphEntityRepository<XtdValue> {
}
