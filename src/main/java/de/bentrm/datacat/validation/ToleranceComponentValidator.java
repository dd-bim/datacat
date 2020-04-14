package de.bentrm.datacat.validation;

import de.bentrm.datacat.graphql.dto.ToleranceComponentInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ToleranceComponentValidator implements ConstraintValidator<ToleranceComponentConstraint, ToleranceComponentInput> {

    Logger logger = LoggerFactory.getLogger(ToleranceComponentValidator.class);

    @Override
    public void initialize(ToleranceComponentConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(ToleranceComponentInput value, ConstraintValidatorContext context) {
        boolean toleranceValueSet = value.getLowerTolerance() != null || value.getUpperTolerance() != null;

        if (value.getToleranceType() != null) {
            return toleranceValueSet;
        } else {
            return !toleranceValueSet;
        }
    }

}
