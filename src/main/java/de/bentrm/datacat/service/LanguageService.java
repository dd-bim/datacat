package de.bentrm.datacat.service;

import de.bentrm.datacat.domain.XtdLanguage;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

public interface LanguageService {

    @PreAuthorize("hasRole('READONLY')")
    boolean isValidLanguage(String language);

    @PreAuthorize("hasRole('READONLY')")
    Optional<XtdLanguage> findByLanguage(String language);

}
