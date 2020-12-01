package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.value.CatalogEntryProperties;
import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogItemSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface CatalogService {

    CatalogStatistics getStatistics();

    /**
     * Constructs and persists a new catalog entry.
     *
     * @param type The xtd type of the new entry.
     * @param properties The properties of the new catalog entry.
     * @return A new persistent catalog entry.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem createCatalogEntry(@NotNull CatalogEntryType type, @Valid CatalogEntryProperties properties);

    /**
     * Deletes a catalog item guarding against accidentially deleting relationship
     * entries. Throws if no entry is found.
     *
     * @param id The id of the catalog entry that should be deleted.
     * @return The deleted entry.
     */
    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem deleteEntry(@NotBlank String id);

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

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Tag createTag(String id, @NotBlank String name);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Tag updateTag(@NotBlank String id, @NotBlank String name);

    @PreAuthorize("hasRole('ADMIN')")
    @NotNull Tag deleteTag(@NotBlank String id);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem addTag(@NotBlank String entryId, @NotBlank String tagId);

    @PreAuthorize("hasRole('USER')")
    @NotNull CatalogItem removeTag(@NotBlank String entryId, @NotBlank String tagId);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull List<CatalogItem> getAllEntriesById(List<String> ids);

    @PreAuthorize("hasRole('READONLY')")
    @NotNull List<XtdRoot> getAllRootItemsById(List<String> ids);

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
    long countRootItems(@NotNull RootSpecification specification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getHierarchy(@NotNull CatalogItemSpecification rootNodeSpecification, int depth);

}
