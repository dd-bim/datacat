package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.UniqueEntity;
import org.springframework.data.neo4j.annotation.Depth;

public interface UniqueEntityRepository<T extends UniqueEntity> extends EntityRepository<T> {

    T findByUniqueId(String uniqueId);
    T findByUniqueId(String uniqueId, @Depth int depth);

}
