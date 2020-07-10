package de.bentrm.datacat.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NodeEntity(label = "Migration")
public class Migration extends Entity {

    private Instant appliedAt;

    private List<String> commands;

}
