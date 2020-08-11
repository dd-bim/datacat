package de.bentrm.datacat.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Version;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Entity {

    public static final String PREFIX = "Xtd";

    @Id
    @GeneratedValue(strategy = IfcGuidStrategy.class)
    @EqualsAndHashCode.Include
    protected String id;

    @Version
    protected Long version;

    @CreatedDate
    protected Instant created;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    protected Instant lastModified;

    @LastModifiedBy
    protected String lastModifiedBy;
}
