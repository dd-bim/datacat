package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface CatalogRecordService<T extends CatalogItem> extends QueryService<T> {

    @NotNull CatalogRecordType getSupportedCatalogRecordType();

    /**
     * Removes a catalog record. Throws if no entry is found.
     *
     * @param id The id of the catalog record that should be deleted.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull T removeRecord(@NotBlank String id);

}
