package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Internal utility service whose primary purpose is to be used by other service
 * implementations.
 */
public interface CatalogCleanupService {

    void purgeRelatedData(@NotBlank String recordId);

    /**
     * Deletes all relationships that are linked to the given
     * record id.
     * @param recordId The relating record that all relationships will be purged from.
     */
    void purgeRelationships(@NotBlank String recordId);

        /**
     * Deletes one specific relationship that is linked to the given
     * record id.
     * @param recordId The relating record there the relationship will be purged from.
     * @param relatedRecordId The related record of the relationship.
     * @param relationType The type of the relationship that will be purged.
     */
    void purgeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId, @NotNull SimpleRelationType relationType);


    /**
     * Deletes the given record and all relationships that are linked to it.
     * @param recordId The record that will be purged.
     */
    void deleteNodeWithRelationships(@NotBlank String recordId);
}
