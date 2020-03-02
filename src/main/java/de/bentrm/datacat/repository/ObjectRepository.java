package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdObject;
import de.bentrm.datacat.domain.XtdSubject;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends NamedEntityRepository<XtdObject> {

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(o:XtdObject)<-[:DOCUMENTS]-(rel:XtdRelDocuments {id: {id}})" +
            "WITH o, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT o SKIP {skip} LIMIT {limit} " +
            "RETURN o, [ p=(o)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], ID(o)"
    )
    Iterable<XtdObject> findByRelDocumentsId(@Param("id") String id, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (o:XtdObject)<-[:DOCUMENTS]-(rel:XtdRelDocuments {id: {id}})" +
            "RETURN count(DISTINCT o)"

    )
    long countByRelDocumentsId(@Param("id") String id);

    @Query(
            "MATCH (name:XtdName)-[:IS_NAME_OF]->(o:XtdObject)<-[:ASSOCIATES]-(rel:XtdRelGroups {id: {id}})" +
            "WITH o, name ORDER BY name.sortOrder, toLower(name.name) ASC, name.name DESC " +
            "WITH DISTINCT o SKIP {skip} LIMIT {limit} " +
            "RETURN o, [ p=(o)<-[:IS_NAME_OF|IS_DESCRIPTION_OF]-() | [relationships(p), nodes(p)] ], ID(o)"
    )
    Iterable<XtdObject> findByRelGroupsId(@Param("id") String id, @Param("skip") int skip, @Param("limit") int limit);

    @Query(
            "MATCH (o:XtdObject)<-[:ASSOCIATES]-(rel:XtdRelGroups {id: {id}})" +
            "RETURN count(DISTINCT o)"

    )
    long countByRelGroupsId(@Param("id") String id);
}
