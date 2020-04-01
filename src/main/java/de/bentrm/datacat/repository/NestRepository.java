package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.collection.XtdNest;
import org.springframework.stereotype.Repository;

@Repository
public interface NestRepository extends GraphEntityRepository<XtdNest, String> {
}
