package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.dto.LanguageInputDto;

import java.util.Optional;

public interface LanguageService extends EntityService<XtdLanguage> {

    XtdLanguage create(LanguageInputDto dto);
    Optional<XtdLanguage> delete(String id);
    Optional<XtdLanguage> findByLanguageRepresentationId(String id);

}
