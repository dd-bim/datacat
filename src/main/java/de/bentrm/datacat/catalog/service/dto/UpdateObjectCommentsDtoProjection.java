package de.bentrm.datacat.catalog.service.dto;

import java.util.Set;

public interface UpdateObjectCommentsDtoProjection extends EntityDtoProjection {
    
    Set<MultiLanguageTextDtoProjection> getComments();
}
