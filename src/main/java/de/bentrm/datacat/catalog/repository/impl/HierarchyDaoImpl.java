package de.bentrm.datacat.catalog.repository.impl;

import de.bentrm.datacat.catalog.repository.HierarchyDao;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HierarchyDaoImpl implements HierarchyDao {

    public static final String QUERY = """               
            MATCH p=(a)-[*0..10]->(c:XtdRoot)              
            WHERE a.id IN $rootIds AND NOT (c)-->(:Relationship)-->(:XtdRoot)                                    
            WITH [x IN nodes(p) | x.id] AS paths
            RETURN DISTINCT paths    
            """;

    private final SessionFactory sessionFactory;

    public HierarchyDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<List<String>> getHierarchyPaths(List<String> rootNodeIds, int depth) {
        final String query = QUERY;
        final Map<String, Object> parameters = Map.of(
                "rootIds", rootNodeIds,
                "depth", depth
        );
        final Session session = sessionFactory.openSession();
        final Result result = session.query(query, parameters);
        final List<List<String>> paths = new ArrayList<>();
        result.forEach(x -> {
            final List<String> arr = List.of((String[]) x.get("paths"));
            paths.add(arr);
        });
        return paths;
    }
}
