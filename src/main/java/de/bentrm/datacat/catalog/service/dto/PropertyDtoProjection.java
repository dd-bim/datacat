package de.bentrm.datacat.catalog.service.dto;

import de.bentrm.datacat.catalog.domain.Enums.XtdDataTypeEnum;

public interface PropertyDtoProjection extends EntityDtoProjection {

    XtdDataTypeEnum getDataType();
    
}
