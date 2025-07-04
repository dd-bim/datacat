package de.bentrm.datacat.catalog.domain;

import org.springframework.data.neo4j.core.schema.Id;

import de.bentrm.datacat.catalog.domain.Enums.XtdUnitBaseEnum;
import de.bentrm.datacat.catalog.domain.Enums.XtdUnitScaleEnum;

import java.time.Instant;
import java.util.Set;

import lombok.Data;

@Data
public class ExportItemResult {
    
    @Id
    public String id;
    public String type;
    public Set<String> tags;
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
    public String languageOfCreator;
    public String countryOfOrigin;
    public String examples;
    public Set<String> languages;
    public String deprecationExplanation;
    public String dataType;
    public String dataFormat;
    public String uri;
    public String author;
    public String publisher;
    public String isbn;
    public Instant dateOfPublication;
    public XtdUnitScaleEnum scale;
    public XtdUnitBaseEnum base;
}