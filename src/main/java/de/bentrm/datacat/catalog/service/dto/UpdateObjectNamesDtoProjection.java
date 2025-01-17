package de.bentrm.datacat.catalog.service.dto;

import java.util.Set;

public interface UpdateObjectNamesDtoProjection extends EntityDtoProjection {
    
    Set<MultiLanguageTextDtoProjection> getNames();
}
