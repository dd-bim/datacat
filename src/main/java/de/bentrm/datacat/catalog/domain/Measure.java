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
@NodeEntity(label = Measure.LABEL)
public class Measure extends XtdObject {

    public static final String LABEL = "XtdMeasureWithUnit";

    @Relationship(type = XtdRelAssignsUnits.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssignsUnits> assignedUnits = new HashSet<>();

    @Relationship(type = XtdRelAssignsValues.RELATIONSHIP_TYPE)
    private final Set<XtdRelAssignsValues> assignedValues = new HashSet<>();

    @Relationship(type = XtdRelAssignsMeasures.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssignsMeasures> assignedTo = new HashSet<>();
}
