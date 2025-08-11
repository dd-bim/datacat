package de.bentrm.datacat.graphql.input;

import lombok.Data;

@Data
public class DimensionInput {
    RationalInput amountOfSubstanceExponent;
    RationalInput electricCurrentExponent;
    RationalInput lengthExponent;
    RationalInput luminousIntensityExponent;
    RationalInput massExponent;
    RationalInput thermodynamicTemperatureExponent;
    RationalInput timeExponent;
}
