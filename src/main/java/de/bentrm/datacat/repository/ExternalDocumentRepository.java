package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalDocumentRepository extends NamedEntityRepository<XtdExternalDocument> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(document:XtdExternalDocument) " +
            "WITH document, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT document SKIP {skip} LIMIT {limit} " +
            "RETURN document, [ p=(document)<-[:IS_NAME_OF]-() | [relationships(p), nodes(p)] ], ID(document)"
    )
    Iterable<XtdExternalDocument> findAllOrderedByName(@Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node:XtdName)-[:IS_NAME_OF]->(document:XtdExternalDocument) " +
            "WITH DISTINCT document SKIP {skip} LIMIT {limit} " +
            "RETURN document, [ p=(document)<-[:IS_NAME_OF]-() | [relationships(p), nodes(p)] ], ID(document)"
    )
    Iterable<XtdExternalDocument> findByTerm(@Param("term") String term, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node:XtdName)-[:IS_NAME_OF]->(document:XtdExternalDocument) " +
            "WITH DISTINCT (document) RETURN count(document)"
    )
    long countByTerm(@Param("term") String term);

}
