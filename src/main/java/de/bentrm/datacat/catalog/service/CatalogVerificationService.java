package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.service.value.VerificationConnection;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CatalogVerificationService {

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getPropGroupWithoutProp(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getPropWithoutSubjectOrPropGroup(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getThemeWithoutSubject(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getSubjectWithoutProp(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getValueListWithoutProp(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getUnitWithoutValueList(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getValueWithoutValueList(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMissingTags(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMissingEnglishName(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMultipleIDs(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMissingDescription(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMissingEnglishDescription(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMultipleNames(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMultipleNamesAcrossClasses(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMissingDictionary(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getMissingReferenceDocument(Pageable pageable);

    @PreAuthorize("hasRole('READONLY')")
    VerificationConnection getInactiveConcepts(Pageable pageable);
}
