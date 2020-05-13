package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;

public interface SearchService {

    @PreAuthorize("isAuthenticated()")
    Page<XtdEntity> search(@NotNull FilterOptions<String> filterOptions, @NotNull Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    long countSearchResults(@NotNull FilterOptions<String> filterOptions);

}
