package de.bentrm.datacat.catalog.service;

import java.util.Optional;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdRational;

public interface DimensionRecordService extends SimpleRecordService<XtdDimension> {

    Optional<XtdRational> getThermodynamicTemperatureExponent(XtdDimension dimension);
    
    Optional<XtdRational> getElectricCurrentExponent(XtdDimension dimension);

    Optional<XtdRational> getLengthExponent(XtdDimension dimension);

    Optional<XtdRational> getLuminousIntensityExponent(XtdDimension dimension);

    Optional<XtdRational> getAmountOfSubstanceExponent(XtdDimension dimension);

    Optional<XtdRational> getMassExponent(XtdDimension dimension);

    Optional<XtdRational> getTimeExponent(XtdDimension dimension);
}
