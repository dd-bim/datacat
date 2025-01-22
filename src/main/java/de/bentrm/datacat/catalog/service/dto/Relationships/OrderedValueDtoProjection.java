package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface OrderedValueDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getOrderedValue();
}
