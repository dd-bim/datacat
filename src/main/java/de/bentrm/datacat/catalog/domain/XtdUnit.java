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
@NodeEntity(label = XtdUnit.LABEL)
public class XtdUnit extends XtdConcept {
    public static final String LABEL = "XtdUnit";

    // Specifies whether the scale of a unit is linear or logarithmic.
    private XtdUnitScaleEnum scale;

    // Allows to introduce irrational numbers.
    private XtdUnitBaseEnum base;

    // // Optional symbol denominating the unit.
    // @ToString.Include
    // @Relationship(type = "SYMBOLS")
    // private final XtdMultiLanguageText symbol; // XtdMultiLanguageText anlegen

    // Offset of origins.
    private XtdRational offset;

    // Proportionality factor. See ISO 80000-1:2009.
    private XtdRational coefficient;

    // Dimension of the unit.
    private XtdDimension dimension; // XtdDimension anlegen


    @Relationship(type = XtdRelAssignsUnits.RELATIONSHIP_TYPE, direction = Relationship.INCOMING)
    private final Set<XtdRelAssignsUnits> assignedTo = new HashSet<>();
}
