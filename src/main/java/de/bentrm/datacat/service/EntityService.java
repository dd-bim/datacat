package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface EntityService<T extends Entity> {

    @NotNull Optional<T> findById(@NotNull String id);

    @NotNull Page<T> findByIds(@NotNull List<String> ids, @NotNull Pageable pageable);

    @NotNull Page<T> findAll(@NotNull Pageable pageable);

    @NotNull Page<T> findByTerm(@NotBlank String term, @NotNull Pageable pageable);

}
