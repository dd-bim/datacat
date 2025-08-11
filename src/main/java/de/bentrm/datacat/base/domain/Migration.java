package de.bentrm.datacat.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.Instant;
import java.util.List;

/**
 * The migration entity is used to log the successful application
 * of database migrations. The id of the migration equals the filename of the
 * applied migration file.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Node("Migration")
public class Migration extends Entity {

    private Instant appliedAt;

    /**
     * The CQL statements that have been applied.
     */
    private List<String> commands;

}
