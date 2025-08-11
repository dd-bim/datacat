package de.bentrm.datacat.catalog.service.dto;

import de.bentrm.datacat.catalog.domain.Enums.XtdPropertyRelationshipTypeEnum;

public interface RelationshipToPropertyDtoProjection {
    
    XtdPropertyRelationshipTypeEnum getRelationshipType();
}
