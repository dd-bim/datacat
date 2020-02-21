package de.bentrm.datacat.repository.collection;

import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NestRepository extends NamedEntityRepository<XtdNest> {
}
