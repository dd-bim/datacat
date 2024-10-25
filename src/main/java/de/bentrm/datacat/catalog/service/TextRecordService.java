package de.bentrm.datacat.catalog.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;

import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdLanguage;

public interface TextRecordService extends SimpleRecordService<XtdText> {

    XtdLanguage getLanguage(@NotNull XtdText text);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdText updateText(@NotBlank String commentId, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdText deleteText(@NotBlank String commentId);
}
