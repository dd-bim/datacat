package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.GraphEntityRepository;
import de.bentrm.datacat.catalog.domain.XtdActor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends GraphEntityRepository<XtdActor> {
}
