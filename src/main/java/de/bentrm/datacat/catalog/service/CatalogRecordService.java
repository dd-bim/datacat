package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;

import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface CatalogRecordService<T extends CatalogRecord> extends QueryService<T> {

    @NotNull CatalogRecordType getSupportedCatalogRecordType();

    /**
     * Removes a catalog record. Throws if no entry is found.
     *
     * @param id The id of the catalog record that should be deleted.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull T removeRecord(@NotBlank String id);

    /**
     * Allows to remove a relationship of a catalog record.
     *
     * @param recordId          The id of the catalog record node to set related records.
     * @param relatedRecordId   The id of the catalog record that is related by relationship.
     * @param relationType      The type of the relationship.
     * @return The updated catalog record.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull T removeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId, @NotNull SimpleRelationType relationType);
}
