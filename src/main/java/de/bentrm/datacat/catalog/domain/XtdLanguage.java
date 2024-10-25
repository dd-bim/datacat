package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdLanguage.LABEL)
public class XtdLanguage extends XtdRoot {

    public static final String LABEL = "XtdLanguage";

    private String englishName;

    private String nativeName;

    private String code;

    private List<String> comments;
}
