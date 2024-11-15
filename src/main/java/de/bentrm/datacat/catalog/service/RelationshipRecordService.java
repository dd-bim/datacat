package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.AbstractRelationship;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Marker interface to designate service components handling relationships.
 *
 * @param <T>
 */
public interface RelationshipRecordService<T extends AbstractRelationship>
        extends CatalogRecordService<T>, QueryService<T> {

    /**
     * Creates a new relationship record.
     * @param id The entity id of the relationship node.
     * @param relatingRecordId The id of the catalog record node that this relationships originates from.
     * @param relatedRecordIds The ids of the catalog records that are members of this relationship.
     * @return The new relationship entity.
     */
    T addRecord(@NotBlank String id,
                @NotBlank String relatingRecordId,
                @NotEmpty List<@NotBlank String> relatedRecordIds);

    /**
     * Allows to update the members of a relationship record.
     * @param relationshipId The id of the catalog record node that this relationships originates from.
     * @param relatedRecordIds The ids of the catalog records that are members of this relationship.
     * @return The updated relationship record.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull T setRelatedRecords(@NotBlank String relationshipId,
                                 @NotEmpty List<@NotBlank String> relatedRecordIds);


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
