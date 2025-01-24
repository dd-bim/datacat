package de.bentrm.datacat.catalog.repository;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RootRepository extends EntityRepository<XtdRoot> {

    @Query("""
                    MATCH (start:XtdObject)
            WHERE start.id IN $startIds
            MATCH path = (start)-[*]->(end:XtdObject)
            WITH [x IN nodes(path) WHERE x:XtdObject | x.id] AS paths
            RETURN paths
                """)
    List<List<String>> findRelationshipPaths(List<String> startIds);

}
