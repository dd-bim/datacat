package de.bentrm.datacat.catalog.repository.impl;

import de.bentrm.datacat.catalog.repository.NestRepositoryExtension;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.Map;

public class NestRepositoryExtensionImpl implements NestRepositoryExtension {

    private final SessionFactory sessionFactory;

    public NestRepositoryExtensionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<String> findGroupOfPropertiesIdBySubjectId(String id) {
        final String query = """
               MATCH (s:XtdSubject)-->(:XtdRelAssignsCollections)-->(n:XtdNest)
               WHERE s.id=$id
               AND (n)-[:TAGGED]->(:Tag {id: "a27c8e3c-5fd1-47c9-806a-6ded070efae8"})
               RETURN n.id""";
        final Session session = sessionFactory.openSession();
        return session.query(String.class, query, Map.of("id", id));
    }
}
