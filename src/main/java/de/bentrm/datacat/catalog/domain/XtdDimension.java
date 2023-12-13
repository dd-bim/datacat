package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@NodeEntity(label = XtdDimension.LABEL)
public class XtdDimension extends XtdConcept {

    public static final String LABEL = "XtdDimension";

    private XtdRational ThermodynamicTemperatureExponent;

    private XtdRational ElectricCurrentExponent;

    private XtdRational TimeExponent;

    private XtdRational MassExponent;

    private XtdRational LengthExponent;

    private XtdRational LuminousIntensityExponent;

    private XtdRational AmountOfSubstanceExponent;

}
