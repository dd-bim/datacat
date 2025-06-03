package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface ValueDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getOrderedValue();
}
