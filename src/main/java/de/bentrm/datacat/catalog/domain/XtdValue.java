package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Node(XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String LABEL = "XtdValue";

    private String nominalValue;

    @Relationship(type = "ORDERED_VALUE", direction = Relationship.Direction.INCOMING)
    private final Set<XtdOrderedValue> orderedValues = new HashSet<>();
}
