package de.bentrm.datacat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public abstract class Entity {

	public static final String PREFIX = "Xtd";

	@Id
	@GeneratedValue(strategy = IfcGuidStrategy.class)
	protected String id;

	@Version
	protected Long version;

	@CreatedDate
	protected LocalDateTime created;

	@LastModifiedDate
	protected LocalDateTime lastModified;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("version", version)
				.append("created", created)
				.append("lastModified", lastModified)
				.toString();
	}
}
