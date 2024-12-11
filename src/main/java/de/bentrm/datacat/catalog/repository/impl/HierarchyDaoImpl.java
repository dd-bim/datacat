package de.bentrm.datacat.catalog.repository.impl;

import de.bentrm.datacat.catalog.repository.HierarchyDao;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;

@Component
public class HierarchyDaoImpl implements HierarchyDao {

    public static final String QUERY = """               
            MATCH p=(a)-[*0..10]->(c:XtdRoot)              
            WHERE a.id IN $rootIds AND NOT (c)-->(:Relationship)-->(:XtdRoot)                                    
            WITH [x IN nodes(p) | x.id] AS paths
            RETURN DISTINCT paths    
            """;

    private final Neo4jClient neo4jClient;

    public HierarchyDaoImpl(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public List<List<String>> getHierarchyPaths(List<String> rootNodeIds, int depth) {
        final String query = QUERY;
        final Map<String, Object> parameters = Map.of(
                "rootIds", rootNodeIds,
                "depth", depth
        );
        final Collection<Map<String, Object>> result = neo4jClient.query(query).bindAll(parameters).fetch().all();
        final List<List<String>> paths = new ArrayList<>();
        for (Map<String, Object> x : result) {
            final List<String> arr = List.of((String[]) x.get("paths"));
            paths.add(arr);
        }
        return paths;
    }
}
