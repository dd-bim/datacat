package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdUnit;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends GraphEntityRepository<XtdUnit, String> {
}
