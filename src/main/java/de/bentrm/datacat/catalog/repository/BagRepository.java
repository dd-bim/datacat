package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdBag;
import org.springframework.stereotype.Repository;

@Repository
public interface BagRepository extends EntityRepository<XtdBag> {

}
