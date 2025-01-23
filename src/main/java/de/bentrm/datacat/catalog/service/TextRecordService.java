package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;

import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.graphql.dto.TextCountResult;
import de.bentrm.datacat.catalog.domain.XtdLanguage;

public interface TextRecordService extends SimpleRecordService<XtdText> {

    XtdLanguage getLanguage(@NotNull XtdText text);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdText updateText(@NotBlank String commentId, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdText deleteText(@NotBlank String commentId);

    @PreAuthorize("hasRole('USER')")
    @NotNull TextCountResult countTexts(@NotBlank String commentId);
}
