package de.bentrm.datacat.service;

import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.service.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface TagService {

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull TagDto create(@Valid TagDto dto);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull TagDto update(@Valid TagDto dto);

    @PreAuthorize("hasRole('ADMIN')")
    TagDto delete(@NotNull String id);

    @PreAuthorize("hasRole('USER')")
    @NotNull TagDto findById(@NotNull String id);

    @PreAuthorize("hasRole('USER')")
    @NotNull Page<TagDto> findAll(@NotNull TagSpecification specification);
}
