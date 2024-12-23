package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface ConceptRecordService extends SimpleRecordService<XtdConcept> {

    List<XtdExternalDocument> getReferenceDocuments(@NotNull XtdConcept concept);

    List<XtdMultiLanguageText> getExamples(@NotNull XtdConcept concept);

    Optional<XtdCountry> getCountryOfOrigin(@NotNull XtdConcept concept);

    List<XtdConcept> getSimilarConcepts(@NotNull XtdConcept concept);

    Optional<XtdMultiLanguageText> getDefinition(@NotNull XtdConcept concept);

    Optional<XtdLanguage> getLanguageOfCreator(@NotNull XtdConcept concept);

    List<XtdMultiLanguageText> getDescriptions(@NotNull XtdConcept concept);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdConcept addDescription(@NotBlank String id, String descriptionId, @NotBlank String languageTag, @NotBlank String value);

}
