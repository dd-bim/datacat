package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.service.value.CatalogRecordProperties;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Designates service components handling simple catalog records,
 * non-relationship records that is.
 *
 * @param <T>
 */
public interface SimpleRecordService<T extends CatalogItem>
        extends CatalogRecordService<T>, QueryService<T> {

    /**
     * Constructs and persists a new catalog entry.
     *
     * @param properties The properties of the new catalog entry.
     * @return A new persistent catalog entry.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem addRecord(@Valid CatalogRecordProperties properties);

}
