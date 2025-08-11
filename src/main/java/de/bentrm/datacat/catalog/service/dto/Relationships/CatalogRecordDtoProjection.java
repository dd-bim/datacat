package de.bentrm.datacat.catalog.service.dto.Relationships;

import java.util.Set;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;
import de.bentrm.datacat.catalog.service.dto.TagDtoProjection;

public interface CatalogRecordDtoProjection extends EntityDtoProjection {
    
    Set<TagDtoProjection> getTags();

}
