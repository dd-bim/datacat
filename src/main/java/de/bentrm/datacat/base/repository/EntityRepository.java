package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.base.domain.Entity;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends GraphEntityRepository<Entity> {

}
