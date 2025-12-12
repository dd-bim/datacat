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
@Node(XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String LABEL = "XtdValue";

    private String nominalValue;

    @Lazy
    @Relationship(type = "ORDERED_VALUE", direction = Relationship.Direction.INCOMING)
    private Set<XtdOrderedValue> orderedValues = new HashSet<>();
}
