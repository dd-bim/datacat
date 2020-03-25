package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelGroupsRepository extends Neo4jRepository<XtdRelGroups, String>, RelGroupsRepositoryExtension {

//    @Query(
//            "MATCH (name:XtdName)-[:IS_NAME_OF]->(obj:XtdObject) " +
//            "WITH obj, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
//            "MATCH (obj)-[:GROUPS]->(rel:XtdRelGroups) " +
//            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
//            "RETURN rel, [ " +
//                "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
//                "[ q=(rel)<-[:GROUPS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
//            "], ID(rel)"
//    )
//    Iterable<XtdRelGroups> findAllOrderedByRelatingObjectName(@Param("skip") long skip, @Param("limit") int limit);
//
//    @Query(
//            "MATCH (name:XtdName)-[:IS_NAME_OF]->(obj:XtdObject {id: {id}}) " +
//            "WITH obj, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
//            "MATCH (obj)-[:GROUPS]->(rel:XtdRelGroups) " +
//            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
//            "RETURN rel, [ " +
//                "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
//                "[ q=(rel)-[:GROUPS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
//            "], ID(rel)"
//    )
//    Iterable<XtdRelGroups> findByRelatingObjectOrderedByRelatingObjectName(
//            @Param("id") String id,
//            @Param("skip") long skip,
//            @Param("limit") int limit);
//
//    @Query(
//            "MATCH (:XtdObject {id: {id}})-[:GROUPS]->(rel:XtdRelGroups) " +
//            "RETURN count(DISTINCT rel)"
//    )
//    int countByRelatingObject(@Param("id") String id);
//
//    @Query(
//            "MATCH (name:XtdName)-[:IS_NAME_OF]->(obj:XtdObject {id: {id}}) " +
//            "WITH obj, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
//            "MATCH (obj)<-[:GROUPS]-(rel:XtdRelGroups) " +
//            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
//            "RETURN rel, [ " +
//            "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
//            "[ q=(rel)-[:GROUPS]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
//            "], ID(rel)"
//    )
//    Iterable<XtdRelGroups> findByRelatedObject(@Param("id") String id,
//                                               @Param("skip") long skip,
//                                               @Param("limit") int limit);
//
//    @Query(
//            "MATCH (:XtdObject {id: {id}})<-[:GROUPS]-(rel:XtdRelGroups) " +
//            "RETURN count(DISTINCT rel)"
//    )
//    int countByRelatedObject(@Param("id") String id);
}
