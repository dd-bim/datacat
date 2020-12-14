package de.bentrm.datacat.catalog.service;

import javax.validation.constraints.NotBlank;

/**
 * Internal utility service whose primary purpose is to be used by other service
 * implementations.
 */
public interface CatalogCleanupService {

    void purgeRelatedData(@NotBlank String recordId);

    /**
     * Deletes all translation nodes that are linked to the given
     * record id.
     * @param recordId The record that all persistent translations will be purged from.
     */
    void purgeTranslations(@NotBlank String recordId);

    /**
     * Deletes all relationships that are linked to the given
     * record id.
     * @param recordId The relating record that all relationships will be purged from.
     */
    void purgeRelationships(@NotBlank String recordId);

}
