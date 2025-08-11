package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Designates service components handling simple catalog records,
 * non-relationship records that is.
 *
 * @param <T>
 */
public interface SimpleRecordService<T extends CatalogRecord>
        extends CatalogRecordService<T> {

    /**
     * Constructs and persists a new catalog entry.
     *
     * @param properties The properties of the new catalog entry.
     * @return A new persistent catalog entry.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogRecord addRecord(@Valid CatalogEntryPropertiesInput properties);

        /**
     * Allows to update the members of a catalog record.
     * @param recordId The id of the catalog record node to set related records.
     * @param relatedRecordIds The ids of the catalog records that are members of this relationship.
     * @param relationType The type of the relationship.
     * @return The updated catalog record.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull T setRelatedRecords(@NotBlank String recordId,
                                 @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType);
}
