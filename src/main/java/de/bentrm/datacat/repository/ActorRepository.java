package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdActor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends GraphEntityRepository<XtdActor> {
}
