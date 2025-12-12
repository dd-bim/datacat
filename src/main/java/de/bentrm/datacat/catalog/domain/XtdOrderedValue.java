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
@Node(XtdOrderedValue.LABEL)
public class XtdOrderedValue extends XtdObject {

    public static final String LABEL = "XtdOrderedValue";

    private int order;

    // @ToString.Include
    @Relationship(type = "ORDERED_VALUE")
    private XtdValue orderedValue;

    // @ToString.Include
    @Lazy
    @Relationship(type = "VALUES", direction = Relationship.Direction.INCOMING)
    private Set<XtdValueList> valueLists = new HashSet<>();

}
