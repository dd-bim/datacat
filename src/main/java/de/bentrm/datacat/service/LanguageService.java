package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdLanguage;

import java.util.Optional;

public interface LanguageService {

    boolean isValidLanguage(String language);
    Optional<XtdLanguage> findByLanguage(String language);

}
