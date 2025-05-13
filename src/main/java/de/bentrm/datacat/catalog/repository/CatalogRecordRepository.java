package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.graphql.dto.CatalogRecordStatistics;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRecordRepository extends EntityRepository<CatalogRecord> {

    // Counts, how many of each concepts are in the database.
    @Query("CALL apoc.meta.stats() YIELD labels UNWIND keys(labels) AS id RETURN id, labels[id] AS count")
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
    Long count(String regex, String languageTag);

    @Query("MATCH (cr:CatalogRecord{id: $id})-[:TAGGED]->(t:Tag) RETURN t.id as id, t.name as name, t.createdBy as createdBy, t.created as created, t.lastModified as lastModified, t.lastModifiedBy as lastModifiedBy")
    List<Tag> findTagsByCatalogRecordId(String id);
}
