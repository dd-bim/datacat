package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.service.value.export.*;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CatalogExportService {

    @PreAuthorize("hasRole('READONLY')")
    findExportCatalogRecordsValue getfindExportCatalogRecords();

    @PreAuthorize("hasRole('READONLY')")
    findExportCatalogRecordsRelationshipsValue getfindExportCatalogRecordsRelationships();

}
