package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface RootService<T extends XtdRoot> extends EntityService<T> {

    @NotNull T create(@Valid RootInput dto);
    @NotNull T update(@Valid RootUpdateInput dto);
    Optional<T> delete(@NotNull String id);
}
