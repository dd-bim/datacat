package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;

public interface DictionaryRecordService extends SimpleRecordService<XtdDictionary> {

    @NotNull XtdMultiLanguageText getName(@NotNull XtdDictionary dictionary);

    @NotNull List<XtdObject> getConcepts(@NotNull XtdDictionary dictionary);
}
