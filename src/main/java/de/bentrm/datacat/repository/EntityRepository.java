package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.Entity;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends GraphEntityRepository<Entity> {

}
