package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.query.FilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface EntityService<T extends Entity> {

    @PreAuthorize("isAuthenticated()")
    @NotNull Optional<T> findById(@NotNull String id);

    @PreAuthorize("isAuthenticated()")
    @NotNull Page<T> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    @NotNull Page<T> findAll(@NotNull Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    @NotNull long countAll();

    @PreAuthorize("isAuthenticated()")
    @NotNull Page<T> findAll(@NotNull FilterOptions<String> filterOptions, @NotNull Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    @NotNull long countAll(@NotNull FilterOptions<String> filterOptions);

    @PreAuthorize("isAuthenticated()")
    @NotNull Page<T> findByTerm(@NotBlank String term, @NotNull Pageable pageable);

}
