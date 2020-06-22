package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.CatalogItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CatalogItemRepository extends GraphEntityRepository<CatalogItem>, CatalogItemRepositoryExtension {

    @Query("CALL apoc.meta.stats() YIELD labels RETURN labels")
    Map<String, Long> statistics();

}
