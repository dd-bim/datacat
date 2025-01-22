package de.bentrm.datacat.catalog.service.dto.Relationships;

import de.bentrm.datacat.catalog.service.dto.EntityDtoProjection;

public interface SymbolDtoProjection extends EntityDtoProjection {
    
    EntityDtoProjection getSymbol();
}
