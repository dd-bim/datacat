package de.bentrm.datacat.validation;

import de.bentrm.datacat.domain.XtdValueRoleEnum;
import de.bentrm.datacat.domain.XtdValueTypeEnum;
import de.bentrm.datacat.graphql.dto.ValueComponentInput;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueComponentValidator implements ConstraintValidator<ToleranceComponentConstraint, ValueComponentInput> {

    @Override
    public void initialize(ToleranceComponentConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(ValueComponentInput value, ConstraintValidatorContext context) {
        final XtdValueTypeEnum valueType = value.getValueType();
        switch (valueType) {
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
            default: {}
        }

        // if role is set, only numeric values are valid nominal values
        return value.getValueRole() == XtdValueRoleEnum.Nil || !XtdValueTypeEnum.NUMERIC_VALUE_TYPES.contains(valueType);
    }

}
