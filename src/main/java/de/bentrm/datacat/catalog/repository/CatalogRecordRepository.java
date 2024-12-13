package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.graphql.dto.CatalogRecordStatistics;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CatalogRecordRepository extends EntityRepository<CatalogRecord> {

    // Counts, how many of each concepts are in the database.
    // @Query("CALL apoc.meta.stats() YIELD labels RETURN labels")
    // @Query("CALL apoc.meta.stats() YIELD labels RETURN keys(labels) AS id, labels[keys(labels)] AS count")
    @Query("CALL apoc.meta.stats() YIELD labels UNWIND keys(labels) AS id RETURN id, labels[id] AS count")
    // Map<String, Long> statistics();
    List<CatalogRecordStatistics> statistics();


    @Query("""
        MATCH (l)<-[:LANGUAGE]-(t)<-[:TEXTS]-(x)<-[:NAMES|DESCRIPTIONS]-(n:XtdObject)
        WHERE t.text CONTAINS $regex AND l.code STARTS WITH $languageTag
        RETURN n.id
        SKIP $skip LIMIT $limit
    """)
    List<String> findIdByRegex(String regex, String languageTag, int limit, int skip);

    @Query("""
        MATCH (l)<-[:LANGUAGE]-(t)<-[:TEXTS]-(x)<-[:NAMES|DESCRIPTIONS]-(n:XtdObject)
        WHERE t.text CONTAINS $regex AND l.code STARTS WITH $languageTag
        RETURN COUNT(n.id)
    """)
    long count(String regex, String languageTag);

}
