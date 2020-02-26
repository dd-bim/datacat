package de.bentrm.datacat.repository.relationship;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.repository.NamedEntityRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RelGroupsRepository extends NamedEntityRepository<XtdRelGroups> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(obj:XtdObject) " +
            "WITH obj, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "MATCH (obj)-[:ASSOCIATES]->(rel:XtdRelGroups) " +
            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
            "RETURN rel, [ " +
                "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
                "[ q=(rel)<-[:ASSOCIATES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
            "], ID(rel)"
    )
    Iterable<XtdRelGroups> findAllOrderedByRelatingObjectName(@Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(obj:XtdObject {uniqueId: {uniqueId}}) " +
            "WITH obj, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "MATCH (obj)-[:ASSOCIATES]->(rel:XtdRelGroups) " +
            "WITH DISTINCT rel SKIP {skip} LIMIT {limit} " +
            "RETURN rel, [ " +
                "[ p=(rel)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], " +
                "[ q=(rel)<-[:ASSOCIATES]-()<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(q), nodes(q)] ] " +
            "], ID(rel)"
    )
    Iterable<XtdRelGroups> findByRelatingObjectOrderedByRelatingObjectName(
            @Param("uniqueId") String uniqueId,
            @Param("skip") int skip,
            @Param("limit") int limit);

    @Query(
            "MATCH (:XtdObject {uniqueId: {uniqueId}})-[:ASSOCIATES]->(rel:XtdRelGroups) " +
            "RETURN count(DISTINCT rel)"
    )
    int countByRelatingObject(@Param("uniqueId") String uniqueId);

}
