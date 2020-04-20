package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.SearchOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityRepositoryExtension {

    Page<XtdEntity> search(SearchOptions<String> searchOptions, Pageable pageable);
}
