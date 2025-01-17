package de.bentrm.datacat.catalog.service.dto;

import java.util.Set;

public interface MultiLanguageTextDtoProjection extends EntityDtoProjection{
    
    Set<TextDtoProjection> getTexts();
}
