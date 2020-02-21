package de.bentrm.datacat.repository.collection;

import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagRepository extends NamedEntityRepository<XtdBag> {

}
