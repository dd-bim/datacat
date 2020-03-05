package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.NamedEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NamedEntityRepository<T extends NamedEntity> extends EntityRepository<T> {

    @Query("MATCH (n)<-[:IS_NAME_OF]-(name:XtdName) " +
            "WHERE {label} IN labels(n) " +
            "WITH n, name ORDER BY name.sortOrder ASC, toLower(name.value) ASC, name.value DESC " +
            "WITH DISTINCT n SKIP {skip} LIMIT {limit} " +
            "RETURN n, " +
                "[ " +
                    "[ p=(n)-[:ASSOCIATES]-(:XtdRelGroups)-[:ASSOCIATES]-(member) | [ relationships(p), nodes(p) ] ], " +
                    "[ x=(c)-[:COLLECTS]-(:XtdRelCollects)-[:COLLECTS]->(n) | [ relationships(x), nodes(x) ] ], " +
                    "[ q=(n)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [ relationships(q), nodes(q) ] ], " +
                    "[ r=(member)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(r), nodes(r)] ], " +
                    "[ y=(c)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(y), nodes(y)] ] " +
                "], ID(n)"
    )
    Iterable<T> findAll(@Param("label") String label, @Param("skip") int skip, @Param("limit") int limit);

    @Query("MATCH (n) WHERE {label} IN labels(n) RETURN count(n)")
    int countFindAllResults(@Param("label") String label);

    @Query("MATCH (group)-[:ASSOCIATES]->(:XtdRelGroups)-[:ASSOCIATES]->(n)<-[:IS_NAME_OF]-(name:XtdName) " +
            "WHERE group.id = {groupId} AND {label} IN labels(n) " +
            "WITH n, name ORDER BY name.sortOrder ASC, toLower(name.value) ASC, name.value DESC " +
            "WITH DISTINCT n SKIP {skip} LIMIT {limit} " +
            "RETURN n, " +
                "[ " +
                    "[ p=(n)-[:ASSOCIATES]-(:XtdRelGroups)-[:ASSOCIATES]-(member) | [ relationships(p), nodes(p) ] ], " +
                    "[ q=(n)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [ relationships(q), nodes(q) ] ], " +
                    "[ r=(member)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() |[relationships(r), nodes(r)] ] " +
                "], ID(n)"
    )
    Iterable<T> findAllByGroup(@Param("label") String label, @Param("groupId") String groupId, @Param("skip") int skip, @Param("limit") int limit);

    @Query("MATCH (group)-[:ASSOCIATES]->(:XtdRelGroups)-[:ASSOCIATES]->(n) WHERE group.id = {groupId} AND {label} IN labels(n) RETURN count(n)")
    int countFindAllByGroup(@Param("label") String label, @Param("groupId") String groupId);

    @Query("CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(hit) " +
            "WHERE {label} IN labels(hit) " +
            "WITH DISTINCT hit " +
            "SKIP {skip} LIMIT {limit} " +
            "RETURN hit, " +
                "[ " +
                    "[ p=(hit)-[:ASSOCIATES]-(:XtdRelGroups)-[:ASSOCIATES]-(member) | [ relationships(p), nodes(p) ] ], " +
                    "[ q=(hit)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [ relationships(q), nodes(q) ] ], " +
                    "[ r=(member)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() |[relationships(r), nodes(r)] ] " +
                "], ID(hit)"
    )
    Iterable<T> searchByNameAndDescription(
            @Param("label") String label,
            @Param("term") String term,
            @Param("skip") int skip,
            @Param("limit") int limit);

    @Query("CALL db.index.fulltext.queryNodes('namesAndDescriptions', {term}) YIELD node, score " +
            "MATCH (node)-[:IS_NAME_OF|:IS_DESCRIPTION_OF]->(hit) " +
            "WHERE {label} IN labels(hit) " +
            "WITH DISTINCT hit RETURN count(hit)")
    int countSearchByNameAndDescriptionResults(@Param("label") String label, @Param("term") String term);
}
