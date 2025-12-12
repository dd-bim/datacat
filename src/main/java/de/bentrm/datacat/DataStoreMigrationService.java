package de.bentrm.datacat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.bentrm.datacat.base.domain.Migration;
import de.bentrm.datacat.base.repository.MigrationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom migration service that checks for CQL-migrations on the classpath.
 * Migrations are run ordered by file name. After successful application of a
 * migration to the current database, the ID is logged as a @{@link Migration}
 * entity.
 */
@Slf4j
@Service
public class DataStoreMigrationService implements ApplicationRunner, ResourceLoaderAware {

    private final String MIGRATIONS_RESOURCE_PATTERN = "classpath:migrations/*.cql";

    private final MigrationRepository migrationRepository;
    private final Neo4jClient neo4jClient;
    private ResourceLoader resourceLoader;

    public DataStoreMigrationService(MigrationRepository migrationRepository, Neo4jClient neo4jClient) {
        this.migrationRepository = migrationRepository;
        this.neo4jClient = neo4jClient;
    }

    @Override
    public synchronized void run(ApplicationArguments args) throws Exception {
        log.info("Starting database migration (synchronized execution)...");
        importMigrationFixtures();
        runMigrations();
        log.info("Database migration completed.");
    }

    /**
     * Finds and executes all migrations on the classpath.
     */
    @Transactional
    private void importMigrationFixtures() throws IOException {
        final Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources(MIGRATIONS_RESOURCE_PATTERN);

        log.info("Importing migration resources...");
        for (Resource resource : resources) {
            final String filename = resource.getFilename();
            Assert.notNull(filename, "Filename of migration may not be null");

            @NotNull
            final Optional<Migration> optionalMigration = migrationRepository.findByIdWithDirectRelations(filename);
            if (optionalMigration.isPresent()) {
                // log.info("Skipping already imported migration: {}", filename);
                continue;
            }

            // log.info("Importing new migration resource...");
            final InputStream inputStream = resource.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            List<String> commands = new ArrayList<>();
            StringBuilder commandBuilder = new StringBuilder();

            bufferedReader.lines().forEach(line -> {
                commandBuilder.append(line).append(" ");
                if (line.trim().endsWith(";")) {
                    commands.add(commandBuilder.toString().trim());
                    commandBuilder.setLength(0); // Reset the StringBuilder for the next command
                }
            });

            // Add any remaining command that doesn't end with a semicolon
            if (commandBuilder.length() > 0) {
                commands.add(commandBuilder.toString().trim());
            }
            Migration newMigration = new Migration();
            newMigration.setId(filename);
            newMigration.setCommands(commands);
            newMigration = migrationRepository.save(newMigration);
            log.info("Imported new migration resource {}", newMigration.getId());
        }
        log.info("Finished importing migration resources.");
    }

    @Transactional
    private void runMigrations() {
        final Iterable<Migration> migrations = migrationRepository.findAllMigrationsOrderedById();

        log.info("Starting execution of graph schema migration.");
        for (Migration migration : migrations) {
            if (migration.getAppliedAt() == null) {
                log.info("Applying migration with id {}", migration.getId());
                for (String command : migration.getCommands()) {
                    // log.info("Running migration command: {}", command);
                    neo4jClient.query(command).run();
                    migration.setAppliedAt(Instant.now());
                    migrationRepository.save(migration);

                    log.info("Applied migration command: {}", command);
                }
            } else {
                // log.info("Skipping already applied migration with id {}", migration.getId());
            }
        }
        log.info("Finished execution of graph schema migration.");
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
