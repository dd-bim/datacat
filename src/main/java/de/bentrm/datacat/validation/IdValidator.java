package de.bentrm.datacat.validation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<IdConstraint, String> {

    Logger logger = LoggerFactory.getLogger(LanguageCodeValidator.class);

    @Override
    public void initialize(IdConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !StringUtils.containsWhitespace(value) && !value.isEmpty();
    }

}
