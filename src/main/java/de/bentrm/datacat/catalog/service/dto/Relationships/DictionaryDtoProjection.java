package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface DictionaryDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getDictionary();
}
