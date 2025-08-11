package de.bentrm.datacat.validation;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Locale;

public class LanguageCodeValidator implements ConstraintValidator<LanguageCodeConstraint, String> {

    Logger logger = LoggerFactory.getLogger(LanguageCodeValidator.class);

    @Override
    public void initialize(LanguageCodeConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Locale locale = Locale.forLanguageTag(value);
        return LocaleUtils.isAvailableLocale(locale);
    }
}
