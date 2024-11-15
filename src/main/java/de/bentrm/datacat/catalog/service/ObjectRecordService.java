package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface ObjectRecordService extends SimpleRecordService<XtdObject> {

    XtdDictionary getDictionary(@NotNull XtdObject object);

    XtdMultiLanguageText getDeprecationExplanation(@NotNull XtdObject object);

    List<XtdObject> getReplacedObjects(@NotNull XtdObject object);

    List<XtdObject> getReplacingObjects(@NotNull XtdObject object);

    List<XtdMultiLanguageText> getNames(@NotNull XtdObject object);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdObject addComment(@NotBlank String id, String commentId, @NotBlank String languageTag, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdObject addName(@NotBlank String id, String nameId, @NotBlank String languageTag, @NotBlank String value);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdObject updateStatus(@NotBlank String id, @NotNull XtdStatusOfActivationEnum status);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdObject updateMajorVersion(@NotBlank String id, @NotNull Integer majorVersion);

    @PreAuthorize("hasRole('USER')")
    @NotNull XtdObject updateMinorVersion(@NotBlank String id, @NotNull Integer minorVersion);

}
