package de.bentrm.datacat.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Version;
import org.neo4j.ogm.id.UuidStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NodeEntity("Entity")
public abstract class Entity {

    @Id
    @NotBlank
    @GeneratedValue(strategy = UuidStrategy.class)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @NotNull
    @Version
    @ToString.Include
    private Long version;

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

    public void setId(String id) {
        Assert.hasText(id, "id may not be null or blank.");
        this.id = id.trim();
    }

    public void setCreated(Instant created) {
        Assert.notNull(created, "created may not be null");
        this.created = created;
    }

    public void setCreatedBy(String createdBy) {
        Assert.hasText(createdBy, "createdBy may not be null or blank");
        this.createdBy = createdBy;
    }

    public void setLastModified(Instant lastModified) {
        Assert.notNull(lastModified, "lastModified may not be null");
        this.lastModified = lastModified;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        Assert.hasText(lastModifiedBy, "lastModifiedBy by may not be null or blank");
        this.lastModifiedBy = lastModifiedBy;
    }
}
