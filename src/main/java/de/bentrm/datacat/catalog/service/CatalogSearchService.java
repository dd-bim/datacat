package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CatalogSearchService {

    @PreAuthorize("hasRole('READONLY')")
    Page<XtdObject> search(CatalogRecordSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    public long count(CatalogRecordSpecification specification);

}
