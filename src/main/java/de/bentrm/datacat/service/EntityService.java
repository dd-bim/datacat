package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface EntityService<T extends Entity> {

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<T> findById(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<T> search(@NotNull Specification specification);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<T> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable);

}
