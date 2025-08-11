package de.bentrm.datacat.catalog.service.dto;

public interface ObjectDtoProjection extends EntityDtoProjection {

    int getMajorVersion();
    int getMinorVersion();
    String getStatus(); 
    
}
