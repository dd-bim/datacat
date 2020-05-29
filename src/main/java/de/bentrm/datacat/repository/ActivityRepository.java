package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdActivity;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends GraphEntityRepository<XtdActivity> {
}
