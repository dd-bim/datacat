package de.bentrm.datacat.catalog.domain;

import de.bentrm.datacat.base.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;

/**
 * A tag marks a concept with a certain client defined intent.
 * Concepts can be filtered by tags.
 */
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NodeEntity(label = "Tag")
public class Tag extends Entity {
    @NotBlank
    private String name;
}
