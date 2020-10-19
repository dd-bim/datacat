package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.GraphEntityRepository;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CatalogItemRepository extends GraphEntityRepository<CatalogItem> {

    @Query("CALL apoc.meta.stats() YIELD labels RETURN labels")
    Map<String, Long> statistics();

}
