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

    @ToString.Include
    @Relationship(type = "UNIT")
    private XtdUnit unit;

    @ToString.Include
    @Relationship(type = "LANGUAGE")
    private XtdLanguage language;

    @ToString.Include
    @Relationship(type = "VALUES")
    private final Set<XtdOrderedValue> values = new HashSet<>();

    @ToString.Include
    @Relationship(type = "POSSIBLE_VALUES", direction = Relationship.INCOMING)
    private final Set<XtdProperty> properties = new HashSet<>();
}
