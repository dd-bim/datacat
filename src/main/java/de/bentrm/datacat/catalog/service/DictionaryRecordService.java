package de.bentrm.datacat.catalog.service;

import javax.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;

public interface DictionaryRecordService extends SimpleRecordService<XtdDictionary> {

    XtdMultiLanguageText getName(@NotNull XtdDictionary dictionary);
}
