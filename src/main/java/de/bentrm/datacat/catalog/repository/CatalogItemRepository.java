package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.CatalogItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CatalogItemRepository extends EntityRepository<CatalogItem> {

    @Query("CALL apoc.meta.stats() YIELD labels RETURN labels")
    Map<String, Long> statistics();

    @Query("""
        MATCH (t)<-[:NAMED|DESCRIBED]-(n:CatalogItem)
        WHERE t.label =~ $query AND t.languageCode STARTS WITH $languageTag
        RETURN n.id
        SKIP $skip LIMIT $limit
    """)
    List<String> findIdByRegex(String regex, String languageTag, int limit, int skip);

    @Query("""
        MATCH (t)<-[:NAMED|DESCRIBED]-(n:CatalogItem)
        WHERE t.label =~ $query AND t.languageCode STARTS WITH $languageTag
        RETURN COUNT(n.id)
    """)
    long count(String regex, String languageTag);

}
