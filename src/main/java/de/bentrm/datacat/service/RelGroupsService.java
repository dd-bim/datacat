package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface RelGroupsService extends RelationshipService<XtdRelGroups> {

    @NotNull Page<XtdRelGroups> findByRelatingObjectId(@NotBlank String relatingObjectId, Pageable pageable);
    @NotNull Page<XtdRelGroups> findByRelatedObjectId(@NotBlank String relatedObjectId, Pageable pageable);

}
