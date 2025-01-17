package de.bentrm.datacat.catalog.service.dto;

import java.util.Set;

public interface UpdateConceptDescriptionsDtoProjection extends EntityDtoProjection {
    
    Set<MultiLanguageTextDtoProjection> getDescriptions();
}
