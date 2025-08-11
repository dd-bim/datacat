package de.bentrm.datacat.catalog.service;

import java.util.Optional;

import de.bentrm.datacat.catalog.domain.XtdLanguage;
import jakarta.validation.constraints.NotNull;

public interface LanguageRecordService extends SimpleRecordService<XtdLanguage> {
    
    Optional<XtdLanguage> findByCode(@NotNull String code);
}
