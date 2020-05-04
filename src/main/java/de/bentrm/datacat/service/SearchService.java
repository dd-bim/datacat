package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface SearchService {

    Page<XtdEntity> search(@NotNull FilterOptions<String> filterOptions, @NotNull Pageable pageable);

    long countSearchResults(@NotNull FilterOptions<String> filterOptions);

}
