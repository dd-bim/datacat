package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RootRepository extends EntityRepository<XtdRoot> {

    @Query("""
        MATCH (start:XtdRoot)
        WHERE start.id IN $startIds
        CALL apoc.path.expandConfig(start, {
            beginSequenceAtStart: false,
            sequence: '>, XtdRelationship, >, >XtdRoot'
        }) YIELD path
        WITH [x IN nodes(path) WHERE NOT x:XtdRelationship | x.id] AS paths
        RETURN paths
    """)
    List<List<String>> findRelationshipPaths(List<String> startIds);

}
