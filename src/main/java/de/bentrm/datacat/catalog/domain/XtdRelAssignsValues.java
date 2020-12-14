package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdRelAssignsValues.LABEL)
public class XtdRelAssignsValues extends XtdRelationship {

    public static final String LABEL = "XtdRelAssignsValues";
    public static final String RELATIONSHIP_TYPE = "ASSIGNS_VALUE";

    @Relationship(type = RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private Measure relatingMeasure;

    @Relationship(type = RELATIONSHIP_TYPE)
    private final List<XtdValue> relatedValues = new ArrayList<>();
}
