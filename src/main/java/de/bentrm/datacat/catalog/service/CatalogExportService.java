package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.value.export.*;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface CatalogExportService {

    @PreAuthorize("hasRole('READONLY')")
    findExportCatalogRecordsValue getfindExportCatalogRecords();

    @PreAuthorize("hasRole('READONLY')")
    findExportCatalogRecordsRelationshipsValue getfindExportCatalogRecordsRelationships();

}
