package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdEntity;
import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;

public interface SearchService {

    @PreAuthorize("hasRole('READONLY')")
    Page<XtdEntity> search(@NotNull FilterOptions filterOptions, @NotNull Pageable pageable);

    @PreAuthorize("hasRole('ROLE_READONLY')")
    long countSearchResults(@NotNull FilterOptions filterOptions);

}
