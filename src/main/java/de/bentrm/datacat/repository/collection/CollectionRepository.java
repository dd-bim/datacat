package de.bentrm.datacat.repository.collection;

import de.bentrm.datacat.domain.collection.XtdCollection;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends NamedEntityRepository<XtdCollection> {
}
