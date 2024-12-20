package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;

public interface DictionaryRecordService extends SimpleRecordService<XtdDictionary> {

    @NotNull XtdMultiLanguageText getName(@NotNull XtdDictionary dictionary);
}
