package de.bentrm.datacat.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Base class of all node entities of the application.
 *
 * All sub classes should override the base classes equals, hashCode and toString
 * methods using the project provided Lombok annotations. As the domain schema is
 * heavily relying on inter-object relationships, most relationship properties should not be used
 * for logging.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Node("Entity")
public abstract class Entity {

    @Id
    @NotBlank
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @NotNull
    @CreatedDate
    @ToString.Include
    private Instant created;

    @NotBlank
    @CreatedBy
    @ToString.Include
    private String createdBy;

    @NotNull
    @LastModifiedDate
    @ToString.Include
    private Instant lastModified;

    @NotBlank
    @LastModifiedBy
    @ToString.Include
    private String lastModifiedBy;

    /**
     * Sets the entities id. Must be universally unique and may not be null or blank.
     * @param id The new id.
     */
    public void setId(String id) {
        Assert.hasText(id, "id may not be null or blank.");
        this.id = id.trim();
    }

    /**
     * Sets the entity`s creation instant. Should only be used by the persistance provider.
     * @param created The creation instant.
     */
    public void setCreated(Instant created) {
        Assert.notNull(created, "created may not be null");
        this.created = created;
    }

    /**
     * Sets a string representing the (user) entity that created this entity.
     * @param createdBy The user that created this entity.
     */
    public void setCreatedBy(String createdBy) {
        Assert.hasText(createdBy, "createdBy may not be null or blank");
        this.createdBy = createdBy;
    }

    /**
     * Sets the entity`s last modification instant.
     * @param lastModified The instant this entity has been last modified.
     */
    public void setLastModified(Instant lastModified) {
        Assert.notNull(lastModified, "lastModified may not be null");
        this.lastModified = lastModified;
    }

    /**
     * Sets a string representing the last (user) entity that modified this entity.
     * @param lastModifiedBy The user that modified this entity.
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        Assert.hasText(lastModifiedBy, "lastModifiedBy by may not be null or blank");
        this.lastModifiedBy = lastModifiedBy;
    }
}
