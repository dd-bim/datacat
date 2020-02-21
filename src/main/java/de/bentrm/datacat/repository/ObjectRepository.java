package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdObject;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends NamedEntityRepository<XtdObject> {
}
