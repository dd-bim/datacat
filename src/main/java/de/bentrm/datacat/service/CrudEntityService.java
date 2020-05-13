package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CrudEntityService<T extends XtdEntity, C, U> extends EntityService<T> {

    @PreAuthorize("isAuthenticated()")
    @NotNull T create(@Valid C dto);

    @PreAuthorize("isAuthenticated()")
    @NotNull T update(@Valid U dto);

    @PreAuthorize("isAuthenticated()")
    Optional<T> delete(@NotNull String id);

}
