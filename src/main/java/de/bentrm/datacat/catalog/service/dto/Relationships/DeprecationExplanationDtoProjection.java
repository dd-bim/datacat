package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface DeprecationExplanationDtoProjection extends EntityDtoProjection {
    
    TextsDtoProjection getDeprecationExplanation();
}
