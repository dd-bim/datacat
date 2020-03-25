package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdLanguage;
import de.bentrm.datacat.service.LanguageService;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    Logger logger = LoggerFactory.getLogger(LanguageServiceImpl.class);

    @Override
    public boolean isValidLanguage(String language) {
        Locale locale = Locale.forLanguageTag(language);
        return LocaleUtils.isAvailableLocale(locale);
    }

    @Override
    public Optional<XtdLanguage> findByLanguage(@NotNull String language) {
        Locale locale = Locale.forLanguageTag(language);
        if (!LocaleUtils.isAvailableLocale(locale)) {
            return Optional.empty();
        }
        return Optional.of(new XtdLanguage(locale));
    }
}
