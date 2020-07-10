package de.bentrm.datacat;

import de.bentrm.datacat.domain.Migration;
import de.bentrm.datacat.repository.MigrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MigrationService implements ApplicationRunner, ResourceLoaderAware {

    private final String MIGRATIONS_RESOURCE_PATTERN = "classpath:migrations/*.cql";

    @Autowired
    private MigrationRepository migrationRepository;

    @Autowired
    private SessionFactory sessionFactory;

    private ResourceLoader resourceLoader;

    private Session session;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        session = sessionFactory.openSession();
        importMigrationFixtures();
        runMigrations();
    }

    private void importMigrationFixtures() throws IOException {
        final Resource[] resources = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader)
                .getResources(MIGRATIONS_RESOURCE_PATTERN);

        log.info("Importing migration resources...");
        for (Resource resource : resources) {
            final String filename = resource.getFilename();
            assert filename != null;

            @NotNull final Optional<Migration> optionalMigration = migrationRepository.findById(filename);
            if (optionalMigration.isPresent()) {
                final Migration migration = optionalMigration.get();
                log.info("Skipping already imported migration: {}", migration.getId());
                continue;
            }

            log.info("Importing new migration resource...");
            final InputStream inputStream = resource.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            final List<String> commands = bufferedReader.lines().collect(Collectors.toList());

            Migration newMigration = new Migration();
            newMigration.setId(filename);
            newMigration.setCommands(commands);
            newMigration = migrationRepository.save(newMigration);
            log.info("Imported new migration resource {}", newMigration.getId());
        }
        log.info("Finished importing migration resources.");
    }

    private void runMigrations() {
        final Iterable<Migration> migrations = migrationRepository.findAll(Sort.by("id"));

        log.info("Starting migration of graph schema.");
        for (Migration migration : migrations) {
            if (migration.getAppliedAt() == null) {
                log.info("Applying migration with id {}", migration.getId());
                for (String command : migration.getCommands()) {
                    log.info("Running migration command: {}", command);
                    final Result result = session.query(command, Map.of());
                    migration.setAppliedAt(Instant.now());
                    migrationRepository.save(migration);
                    log.info("Result: {}", result.queryStatistics());
                }
            } else {
                log.info("Skipping already applied migration with id {}", migration.getId());
            }
        }
        log.info("Finished migration of graph schema.");
    }

    @Override
    public void setResourceLoader(@org.jetbrains.annotations.NotNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
