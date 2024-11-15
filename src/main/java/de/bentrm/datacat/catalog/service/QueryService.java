package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.base.specification.QuerySpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface QueryService<T extends Entity> {

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<T> findById(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull List<T> findAllByIds(@NotNull List<String> ids);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<T> findAll(@NotNull QuerySpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull long count(@NotNull QuerySpecification specification);
}
