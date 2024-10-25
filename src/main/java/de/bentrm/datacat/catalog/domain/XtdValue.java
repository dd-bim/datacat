package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String LABEL = "XtdValue";

    private String nominalValue;

    @Relationship(type = "ORDERED_VALUE", direction = Relationship.INCOMING)
    private final Set<XtdOrderedValue> orderedValues = new HashSet<>();
}
