package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.service.value.verification.*;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.constraints.NotNull;

public interface CatalogVerificationService {

    @PreAuthorize("hasRole('READONLY')")
    findPropGroupWithoutPropValue getfindPropGroupWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    findPropWithoutSubjectOrPropGroupValue getfindPropWithoutSubjectOrPropGroup();

    @PreAuthorize("hasRole('READONLY')")
    findModelWithoutGroupValue getfindModelWithoutGroup();

    @PreAuthorize("hasRole('READONLY')")
    findGroupWithoutSubjectValue getfindGroupWithoutSubject();

    @PreAuthorize("hasRole('READONLY')")
    findSubjectWithoutPropValue getfindSubjectWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    findMeasureWithoutPropValue getfindMeasureWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    findUnitWithoutMeasureValue getfindUnitWithoutMeasure();

    @PreAuthorize("hasRole('READONLY')")
    findValueWithoutMeasureValue getfindValueWithoutMeasure();

    @PreAuthorize("hasRole('READONLY')")
    findMissingTagsValue getfindMissingTags(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    findMissingEnglishNameValue getfindMissingEnglishName(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    findMultipleIDsValue getfindMultipleIDs(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    findMissingDescriptionValue getfindMissingDescription(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    findMissingEnglishDescriptionValue getfindMissingEnglishDescription(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    findMultipleNamesValue getfindMultipleNames(@NotNull CatalogRecordSpecification rootNodeSpecification);

    @PreAuthorize("hasRole('READONLY')")
    findMultipleNamesAcrossClassesValue getfindMultipleNamesAcrossClasses(@NotNull CatalogRecordSpecification rootNodeSpecification);
}
