package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdText;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface MultiLanguageTextRecordService extends SimpleRecordService<XtdMultiLanguageText> {

    List<XtdText> getTexts(@NotNull XtdMultiLanguageText multiLanguageText);
}
