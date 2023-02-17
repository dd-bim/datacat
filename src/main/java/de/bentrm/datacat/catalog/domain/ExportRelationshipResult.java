package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.annotation.QueryResult;

import lombok.Data;

@Data
@QueryResult
public class ExportRelationshipResult {
    
    public String entity1;
    public String entity1Type;
    public String relationId;
    public String relationshipType; 
    public String entity2;
    public String entity2Type;
}
