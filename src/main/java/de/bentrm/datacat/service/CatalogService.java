package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.CatalogItem;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import de.bentrm.datacat.specification.CatalogItemSpecification;
import de.bentrm.datacat.specification.RootSpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface CatalogService {

    CatalogStatistics getStatistics();

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<CatalogItem> getCatalogItem(@NotBlank String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<XtdRoot> getRootItem(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<CatalogItem> findAllCatalogItems(@NotNull CatalogItemSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    long countCatalogItems(@NotNull CatalogItemSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<XtdRoot> findAllRootItems(@NotNull RootSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    long countRootItems(@NotNull RootSpecification specification);

}
