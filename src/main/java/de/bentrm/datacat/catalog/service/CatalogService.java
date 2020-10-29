package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogItemSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface CatalogService {

    CatalogStatistics getStatistics();

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdRoot deleteEntry(@NotBlank String id);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdRelationship deleteRelationship(@NotBlank String id);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem setVersion(@NotBlank String id, String versionId, String versionDate);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem addName(@NotBlank String id, String nameId, @NotNull Locale locale, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem updateName(@NotBlank String id, @NotBlank String nameId, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem deleteName(@NotBlank String id, @NotBlank String nameId);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem addDescription(@NotBlank String id, String descriptionId, @NotNull Locale locale, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem updateDescription(@NotBlank String id, @NotBlank String descriptionId, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem deleteDescription(@NotBlank String id, @NotBlank String descriptionId);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem tag(String conceptId, String tagId);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem untag(String conceptId, String tagId);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull List<CatalogItem> getAllEntriesById(List<String> ids);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull List<XtdObject> getAllObjectsById(List<String> ids);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull List<XtdCollection> getAllCollectionsById(List<String> ids);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<CatalogItem> getEntryById(@NotBlank String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<XtdRoot> getRootItem(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<XtdObject> getObject(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<XtdCollection> getCollection(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Optional<XtdRelationship> getRelationship(@NotNull String id);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<CatalogItem> findAllCatalogItems(@NotNull CatalogItemSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    long countCatalogItems(@NotNull CatalogItemSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull Page<XtdRoot> findAllRootItems(@NotNull RootSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    long countRootItems(@NotNull RootSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getHierarchy(@NotNull CatalogItemSpecification rootNodeSpecification, int depth);

}