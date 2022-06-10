package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;

@Data
@QueryResult
public class ExportRelationshipResult {
    
    public String Entity1;
    public String Entity1Type;
    public String RelationId;
    public String RelationshipType; 
    public String Entity2;
    public String Entity2Type;
}
