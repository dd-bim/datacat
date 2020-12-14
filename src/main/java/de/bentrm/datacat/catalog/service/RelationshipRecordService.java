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

    T addRecord(@Valid RelationshipProperties properties,
                @NotBlank String relatingRecordId,
                @NotEmpty List<@NotBlank String> relatedRecordIds);


    @PreAuthorize("hasRole('USER')")
    @NotNull T setRelatedRecords(@NotBlank String relationshipId,
                                 @NotEmpty List<@NotBlank String> relatedRecordIds);


}
