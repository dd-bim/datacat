package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.Association;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface AssociationService<T extends Association> {

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<T> findByRelatingThingId(@NotBlank String relatingObjectId, Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<T> findByRelatedThingId(@NotBlank String relatedObjectId, Pageable pageable);
}
