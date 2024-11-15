package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RootRepository extends EntityRepository<XtdRoot> {

    @Query("""
        MATCH (start:XtdRoot)
        WHERE start.id IN $startIds
        CALL apoc.path.expandConfig(start, {
            beginSequenceAtStart: false,
            sequence: '>, Relationship, >, >XtdRoot'
        }) YIELD path
        WITH [x IN nodes(path) WHERE NOT x:Relationship | x.id] AS paths
        RETURN paths
    """)
    List<List<String>> findRelationshipPaths(List<String> startIds);

}
