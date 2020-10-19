package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.service.value.EntryValue;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;

public interface ItemService<T extends CatalogItem> {

    @PreAuthorize("hasRole('USER')")
    @NotNull T create(EntryValue value);
}
