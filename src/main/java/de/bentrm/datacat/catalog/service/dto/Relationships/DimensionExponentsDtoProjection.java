package de.bentrm.datacat.catalog.service.dto.Relationships;

import java.util.Set;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface DimensionExponentsDtoProjection extends EntityDtoProjection {
    
    Set<EntityDtoProjection> getAmountOfSubstanceExponent();
    Set<EntityDtoProjection> getElectricCurrentExponent();
    Set<EntityDtoProjection> getLengthExponent();
    Set<EntityDtoProjection> getLuminousIntensityExponent();
    Set<EntityDtoProjection> getMassExponent();
    Set<EntityDtoProjection> getThermodynamicTemperatureExponent();
    Set<EntityDtoProjection> getTimeExponent();

}
