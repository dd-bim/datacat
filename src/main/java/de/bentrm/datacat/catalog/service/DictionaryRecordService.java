package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;

public interface DictionaryRecordService extends SimpleRecordService<XtdDictionary> {

    @NotNull XtdMultiLanguageText getName(@NotNull XtdDictionary dictionary);

    @NotNull Page<XtdObject> getConcepts(@NotNull XtdDictionary dictionary, @NotNull Pageable pageable);
}
