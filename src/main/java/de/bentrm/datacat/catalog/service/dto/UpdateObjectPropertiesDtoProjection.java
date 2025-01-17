package de.bentrm.datacat.catalog.service.dto;

public interface UpdateObjectPropertiesDtoProjection extends EntityDtoProjection {

    int getMajorVersion();
    int getMinorVersion();
    String getStatus(); 
    
}
