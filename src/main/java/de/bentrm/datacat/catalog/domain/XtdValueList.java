package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdValueList.LABEL)
public class XtdValueList extends XtdConcept {

    public static final String LABEL = "XtdValueList";

    private XtdUnit unit;

    // private XtdLanguage language; // Language nicht implementiert, wie bisher?

    @Relationship(type = "VALUES")
    private Set<XtdOrderedValue> values = new HashSet<>();
}
