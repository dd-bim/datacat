package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends NamedEntityRepository<XtdSubject> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(s:XtdSubject) " +
            "WITH s, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT s SKIP {skip} LIMIT {limit} " +
            "RETURN " +
                    "s, " +
                    "[ p=(s)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
                    "[ q=(s)-[:ASSOCIATES]-() | [relationships(q), nodes(q)] ], " +
                    "[ r=(s)-[:GROUPS]-() | [relationships(r), nodes(r)] ], " +
                    "ID(s)"
    )
    Iterable<XtdSubject> findAll(@Param("skip") long skip, @Param("limit") int limit);

    @Query(
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(hit:XtdSubject) " +
            "WITH DISTINCT hit SKIP {skip} LIMIT {limit} " +
            "RETURN " +
                    "hit, " +
                    "[ p=(hit)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [ relationships(p), nodes(p) ] ], " +
                    "[ q=(s)-[:ASSOCIATES]-() | [relationships(q), nodes(q)] ], " +
                    "[ r=(s)-[:GROUPS]-() | [relationships(r), nodes(r)] ], " +
                    "ID(hit)"
    )
    Iterable<XtdSubject> findByTerm(@Param("term") String term, @Param("skip") long skip, @Param("limit") int limit);

    @Query(
            "CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(hit:XtdSubject) " +
            "RETURN count(DISTINCT hit)"
    )
    int countByTerm(@Param("term") String term);
}
