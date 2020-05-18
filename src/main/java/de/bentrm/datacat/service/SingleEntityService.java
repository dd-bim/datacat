package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface SingleEntityService {

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<XtdEntity> findById(@NotBlank String id);

}
