package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;

public interface TagService {

    @PreAuthorize("hasRole('USER')")
    @NotNull Tag findById(@NotNull String id);

    @PreAuthorize("hasRole('USER')")
    @NotNull Page<Tag> findAll(@NotNull TagSpecification specification);

    @NotNull long count(@NotNull QuerySpecification specification);
}
