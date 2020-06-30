package de.bentrm.datacat;

import de.bentrm.datacat.query.FullTextIndex;
import org.neo4j.driver.exceptions.ClientException;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IndexInitializer implements ApplicationRunner {

    @Autowired
    private Logger logger;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void run(ApplicationArguments args) {
        for (FullTextIndex index : FullTextIndex.values()) {
            dropIndex(index.getIndexName());
            initializeIndex(index.getIndexName(), index.getTypes(), index.getProperties());
        }
    }

    private void initializeIndex(String name, List<String> types, List<String> properties) {
        final Session session = sessionFactory.openSession();

        logger.info("Creating index {}", name);

        try {
            final String cypher = "CALL db.index.fulltext.createNodeIndex($name, $types, $properties)";
            final Result result = session.query(cypher, Map.ofEntries(
                    Map.entry("name", name),
                    Map.entry("types", types),
                    Map.entry("properties", properties)
            ));
            logger.info(result.queryStatistics().toString());
        } catch (ClientException e) {
            logger.warn("Ignoring error error creating index");
        }
    }

    private void dropIndex(String name) {
        final Session session = sessionFactory.openSession();

        logger.info("Dropping index {}", name);

        try {
            final String cypher = "CALL db.index.fulltext.drop($name)";
            final Result result = session.query(cypher, Map.ofEntries((
                    Map.entry("name", name)
            )));
            logger.info(result.queryStatistics().toString());
        } catch (ClientException e) {
            logger.warn("Ignoring error dropping index.}");
        }
    }
}
