package de.bentrm.datacat.catalog.repository.impl;

import de.bentrm.datacat.catalog.repository.PropertyRepositoryExtension;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.Map;

@Slf4j
public class PropertyRepositoryExtensionImpl implements PropertyRepositoryExtension {

    private final SessionFactory sessionFactory;

    public PropertyRepositoryExtensionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<String> findPropertyIdBySubjectId(String id) {
        final String query = """
                MATCH (s:XtdSubject)-[*]->(p:XtdProperty) 
                WHERE s.id=$id RETURN p.id""";
        final Session session = sessionFactory.openSession();
        return session.query(String.class, query, Map.of("id", id));
    }
}
