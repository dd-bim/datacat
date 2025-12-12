package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdValueList.LABEL)
public class XtdValueList extends XtdConcept {

    public static final String LABEL = "XtdValueList";

    // @ToString.Include
    @Relationship(type = "UNIT")
    private XtdUnit unit;

    // @ToString.Include
    @Relationship(type = "LANGUAGE")
    private XtdLanguage language;

    // @ToString.Include
    @Lazy
    @Relationship(type = "VALUES")
    private Set<XtdOrderedValue> values = new HashSet<>();

    // @ToString.Include
    @Lazy
    @Relationship(type = "POSSIBLE_VALUES", direction = Relationship.Direction.INCOMING)
    private Set<XtdProperty> properties = new HashSet<>();
}
