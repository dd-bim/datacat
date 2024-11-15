package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.value.verification.*;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
