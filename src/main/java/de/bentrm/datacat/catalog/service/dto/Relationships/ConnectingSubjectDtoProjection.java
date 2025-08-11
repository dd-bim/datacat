package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface ConnectingSubjectDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getConnectingSubject();
}
