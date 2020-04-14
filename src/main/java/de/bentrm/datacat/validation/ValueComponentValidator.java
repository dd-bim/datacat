package de.bentrm.datacat.validation;

import de.bentrm.datacat.domain.XtdValueTypeEnum;
import de.bentrm.datacat.graphql.dto.ValueComponentInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueComponentValidator implements ConstraintValidator<ToleranceComponentConstraint, ValueComponentInput> {

    Logger logger = LoggerFactory.getLogger(ValueComponentValidator.class);

    @Override
    public void initialize(ToleranceComponentConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(ValueComponentInput value, ConstraintValidatorContext context) {
        if (value.getValueType() != null) {
            // validate value type
            switch (value.getValueType()) {
                case XtdString:
                    if (value.getNominalValue().isBlank()) {
                        return false;
                    }
                case XtdNumber:
                case XtdReal:
                    try {
                        Double.parseDouble(value.getNominalValue());
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case XtdInteger:
                    try {
                        Integer.parseInt(value.getNominalValue(), 10);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case XtdLogical:
                case XtdBoolean:
                    String nominalValue = value.getNominalValue().trim().toLowerCase();
                    if (!nominalValue.equals("true") && !nominalValue.equals("false")) {
                        return false;
                    }
            }
        }

        // if role is set, only numeric values are valid nominal values
        return value.getValueRole() == null || !XtdValueTypeEnum.NUMERIC_VALUE_TYPES.contains(value.getValueType());
    }

}
