package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.value.RelationshipProperties;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Marker interface to designate service components handling relationships.
 *
 * @param <T>
 */
public interface RelationshipRecordService<T extends XtdRelationship>
        extends CatalogRecordService<T>, QueryService<T> {

    /**
     * Creates a new relationship record.
     * @param properties The entity properties of the relationship node.
     * @param relatingRecordId The id of the catalog record node that this relationships originates from.
     * @param relatedRecordIds The ids of the catalog records that are members of this relationship.
     * @return The new relationship entity.
     */
    T addRecord(@Valid RelationshipProperties properties,
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


}
