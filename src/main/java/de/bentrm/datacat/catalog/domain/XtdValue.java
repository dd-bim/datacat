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
@NodeEntity(label = XtdValue.LABEL)
public class XtdValue extends XtdObject {

    public static final String LABEL = "XtdValue";

    // später entfernen
    private ToleranceType toleranceType;

    private String lowerTolerance;

    private String upperTolerance;

    private ValueRole valueRole;

    private ValueType valueType;
    // bis hier

    private String nominalValue;

    @Relationship(type = XtdRelAssignsValues.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private List<XtdRelAssignsValues> assignedTo = new ArrayList<>();
}
