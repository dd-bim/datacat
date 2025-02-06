package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.core.schema.Id;

import java.time.Instant;
import java.util.LinkedHashSet;

import lombok.Data;

@Data
public class ExportItemResult {
    
    @Id
    public String id;
    public String type;
    public LinkedHashSet<String> tags;
    public String name;
    public String name_en;
    public String description;
    public String description_en;
    public String definition;
    public String definition_en;
    public String createdBy;
    public Instant created;
    public Instant lastModified;
    public String lastModifiedBy;
    public Integer majorVersion;
    public Integer minorVersion;
    public String status;
}