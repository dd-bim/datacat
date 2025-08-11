package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface IntervallMaxMinDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getMinimum();
    EntityDtoProjection getMaximum();
}
