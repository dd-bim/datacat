package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CatalogService {

    CatalogStatistics getStatistics();

    // TODO: PostAuthorize access to restricted items
    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<Entity> getEntity(@NotBlank String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<CatalogItem> getCatalogItem(@NotBlank String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<CatalogItem> searchCatalogItem(@NotNull Specification specification);

}
