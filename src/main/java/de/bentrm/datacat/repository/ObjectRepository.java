package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends NamedEntityRepository<XtdObject> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(o:XtdObject)<-[:DOCUMENTS]-(rel:XtdRelDocuments {uniqueId: {uniqueId}})" +
            "WITH o, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT o SKIP {skip} LIMIT {limit} " +
            "RETURN o, [ p=(o)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], ID(o)"
    )
    Iterable<XtdObject> findByRelDocumentsUniqueId(@Param("uniqueId") String uniqueId, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (o:XtdObject)<-[:DOCUMENTS]-(rel:XtdRelDocuments {uniqueId: {uniqueId}})" +
            "RETURN count(DISTINCT o)"

    )
    long countByRelDocumentsUniqueId(@Param("uniqueId") String uniqueId);

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(o:XtdObject)<-[:ASSOCIATES]-(rel:XtdRelGroups {uniqueId: {uniqueId}})" +
            "WITH o, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT o SKIP {skip} LIMIT {limit} " +
            "RETURN o, [ p=(o)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], ID(o)"
    )
    Iterable<XtdObject> findByRelGroupsUniqueId(@Param("uniqueId") String uniqueId, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (o:XtdObject)<-[:ASSOCIATES]-(rel:XtdRelGroups {uniqueId: {uniqueId}})" +
            "RETURN count(DISTINCT o)"

    )
    long countByRelGroupsUniqueId(@Param("uniqueId") String uniqueId);
}
