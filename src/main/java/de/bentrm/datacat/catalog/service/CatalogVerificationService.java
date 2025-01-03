package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.service.value.HierarchyValue;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.constraints.NotNull;

public interface CatalogVerificationService {

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindPropGroupWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindPropWithoutSubjectOrPropGroup();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindModelWithoutGroup();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindGroupWithoutSubject();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindSubjectWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMeasureWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindUnitWithoutMeasure();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindValueWithoutMeasure();

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMissingTags(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMissingEnglishName(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMultipleIDs(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMissingDescription(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMissingEnglishDescription(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMultipleNames(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    HierarchyValue getfindMultipleNamesAcrossClasses(@NotNull CatalogRecordSpecification rootNodeSpecification);
}
