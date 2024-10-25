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
@NodeEntity(label = XtdDictionary.LABEL)
public class XtdDictionary extends XtdRoot {

    public static final String LABEL = "XtdDictionary";

    @Relationship(type = "NAME")
    private XtdMultiLanguageText name;
}
