package de.bentrm.datacat.base.repository;

import org.springframework.data.neo4j.repository.query.Query;

import de.bentrm.datacat.base.domain.Migration;

public interface MigrationRepository extends EntityRepository<Migration> {

    @Query("MATCH (m:Migration) RETURN m ORDER BY m.id")
    Iterable<Migration> findAllMigrationsOrderedById();
}
