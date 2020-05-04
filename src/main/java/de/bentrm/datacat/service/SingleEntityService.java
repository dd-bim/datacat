package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface SingleEntityService {

    @NotNull Optional<XtdEntity> findById(@NotBlank String id);

}
