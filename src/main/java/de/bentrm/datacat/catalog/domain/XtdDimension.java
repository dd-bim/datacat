package de.bentrm.datacat.catalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@Node(XtdDimension.LABEL)
public class XtdDimension extends XtdConcept {

    public static final String LABEL = "XtdDimension";

    @Relationship(type = "THERMODYNAMIC_TEMPERATURE_EXPONENT")
    private XtdRational thermodynamicTemperatureExponent;

    @Relationship(type = "ELECTRIC_CURRENT_EXPONENT")
    private XtdRational electricCurrentExponent;

    @Relationship(type = "TIME_EXPONENT")
    private XtdRational timeExponent;

    @Relationship(type = "MASS_EXPONENT")
    private XtdRational massExponent;

    @Relationship(type = "LENGTH_EXPONENT")
    private XtdRational lengthExponent;

    @Relationship(type = "LUMINOUS_INTENSITY_EXPONENT")
    private XtdRational luminousIntensityExponent;

    @Relationship(type = "AMOUNT_OF_SUBSTANCE_EXPONENT")
    private XtdRational amountOfSubstanceExponent;

}
