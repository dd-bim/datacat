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
@NodeEntity(label = XtdOrderedValue.LABEL)
public class XtdOrderedValue extends XtdObject {

    public static final String LABEL = "XtdOrderedValue";

    private int order;

    @ToString.Include
    @Relationship(type = "ORDERED_VALUE")
    private XtdValue orderedValue;

    @ToString.Include
    @Relationship(type = "VALUES", direction = Relationship.INCOMING)
    private final Set<XtdValueList> valueLists = new HashSet<>();

}
