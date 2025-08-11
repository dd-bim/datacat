package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.service.value.ExportCatalogRecordsValue;
import de.bentrm.datacat.catalog.service.value.ExportRelationshipsValue;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CatalogExportService {

    @PreAuthorize("hasRole('READONLY')")
    ExportCatalogRecordsValue getExportCatalogRecords();

    @PreAuthorize("hasRole('READONLY')")
    ExportRelationshipsValue getExportCatalogRecordsRelationships();

}
