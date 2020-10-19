package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.GraphEntityRepository;
import de.bentrm.datacat.catalog.domain.XtdObject;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends GraphEntityRepository<XtdObject> {
}
