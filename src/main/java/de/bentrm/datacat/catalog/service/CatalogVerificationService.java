package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.service.value.VerificationValue;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CatalogVerificationService {

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindPropGroupWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindPropWithoutSubjectOrPropGroup();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindModelWithoutGroup();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindGroupWithoutSubject();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindSubjectWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindValueListWithoutProp();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindUnitWithoutValueList();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindValueWithoutValueList();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMissingTags();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMissingEnglishName();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMultipleIDs();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMissingDescription();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMissingEnglishDescription();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMultipleNames();

    @PreAuthorize("hasRole('READONLY')")
    VerificationValue getfindMultipleNamesAcrossClasses();
}
