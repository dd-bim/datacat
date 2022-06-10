package de.bentrm.datacat.catalog.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;

@Data
@QueryResult
public class ExportItemResult {
    
    @Id
    public String id;
    public String typ;
    public String schlagworte;
    public String name;
    public String name_en;
    public String description;
    public String versionId;
    public String createdBy;
    public String created;
    public String lastModified;
    public String lastModifiedBy;
}