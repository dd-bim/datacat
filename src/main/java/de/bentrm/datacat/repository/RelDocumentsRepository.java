package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RelDocumentsRepository extends Neo4jRepository<XtdRelDocuments, String> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(document:XtdExternalDocument) " +
            "WITH document, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "MATCH (document)-[:DOCUMENTS]->(rel:XtdRelDocuments) " +
            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
            "RETURN rel, [ " +
                "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
                "[ q=(rel)<-[:DOCUMENTS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
            "], ID(rel)"
    )
    Iterable<XtdRelDocuments> findAllOrderedByRelatingDocumentName(@Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(document:XtdExternalDocument {id: {id}}) " +
            "WITH document, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "MATCH (document)-[:DOCUMENTS]->(rel:XtdRelDocuments) " +
            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
            "RETURN rel, [ " +
                "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
                "[ q=(rel)<-[:DOCUMENTS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
            "], ID(rel)"
    )
    Iterable<XtdRelDocuments> findByRelatingDocumentOrderedByRelatingDocumentName(@Param("id") String id, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (:XtdExternalDocument {id: {id}})-[:DOCUMENTS]->(rel:XtdRelDocuments) " +
            "RETURN count(DISTINCT rel)"
    )
    int countByRelatingDocument(@Param("id") String id);

}
