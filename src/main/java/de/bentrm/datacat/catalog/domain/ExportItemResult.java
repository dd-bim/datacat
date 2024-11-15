package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.core.schema.Id;
import java.util.List;

import lombok.Data;

@Data
public class ExportItemResult {
    
    @Id
    public String id;
    public String typ;
    public List<String> tags;
    public String name;
    public String name_en;
    public String description;
    public String versionId;
    public String createdBy;
    public String created;
    public String lastModified;
    public String lastModifiedBy;
    // public Integer majorVersion;
    // public Integer minorVersion;
}