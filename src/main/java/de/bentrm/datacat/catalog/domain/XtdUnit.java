package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import de.bentrm.datacat.catalog.domain.Enums.XtdUnitBaseEnum;
import de.bentrm.datacat.catalog.domain.Enums.XtdUnitScaleEnum;

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

    // Optional symbol denominating the unit.
    @ToString.Include
    @Relationship(type = "SYMBOL")
    private XtdMultiLanguageText symbol;

    // Offset of origins.
    @Relationship(type = "OFFSET")
    private XtdRational offset;

    // Proportionality factor. See ISO 80000-1:2009.
    @Relationship(type = "COEFFICIENT")
    private XtdRational coefficient;

    // Dimension of the unit.
    @Relationship(type = "DIMENSION")
    private XtdDimension dimension; 

    @ToString.Include
    @Relationship(type = "UNITS", direction = Relationship.INCOMING)
    private final Set<XtdProperty> properties = new HashSet<>();
}
