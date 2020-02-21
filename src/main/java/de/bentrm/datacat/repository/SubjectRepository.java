package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdName;
import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends NamedEntityRepository<XtdSubject> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(s:XtdSubject) " +
            "WITH s, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT s SKIP {skip} LIMIT {limit} " +
            "RETURN s, [ p=(s)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], ID(s)"
    )
    Iterable<XtdSubject> findAllOrderedByName(@Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(hit:XtdSubject) " +
            "WITH DISTINCT hit SKIP {skip} LIMIT {limit} " +
            "RETURN hit, [ p=(hit)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [ relationships(p), nodes(p) ] ], ID(hit)"
    )
    Iterable<XtdSubject> findByTerm(@Param("term") String term, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(hit:XtdSubject) " +
            "WITH DISTINCT hit RETURN count(hit)"
    )
    int countByTerm(@Param("term") String term);

}
