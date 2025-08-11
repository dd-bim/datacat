package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface RelationshipTypeDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getRelationshipType();
}
