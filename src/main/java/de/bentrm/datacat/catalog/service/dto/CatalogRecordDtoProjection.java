package de.bentrm.datacat.catalog.service.dto;

import java.util.Set;

public interface CatalogRecordDtoProjection extends EntityDtoProjection {
    
    Set<TagDtoProjection> getTags();

}
