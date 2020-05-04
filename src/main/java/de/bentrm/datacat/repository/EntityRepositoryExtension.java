package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityRepositoryExtension {

    Page<XtdEntity> search(FilterOptions<String> searchOptions, Pageable pageable);
}
