package de.bentrm.datacat.catalog.domain;

import lombok.Data;

@Data
public class ExportRelationshipResult {
    
    public String entity1;
    public String relationship; 
    public String entity2;
}
