package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CatalogSearchService {

    @PreAuthorize("hasRole('READONLY')")
    Page<XtdObject> search(@NotNull CatalogRecordSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    public long count(@NotNull CatalogRecordSpecification specification);

}
