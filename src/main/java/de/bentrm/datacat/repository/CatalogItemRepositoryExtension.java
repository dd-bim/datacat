package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CatalogItemRepositoryExtension {

    Page<CatalogItem> search(FilterOptions searchOptions, Pageable pageable);
}
