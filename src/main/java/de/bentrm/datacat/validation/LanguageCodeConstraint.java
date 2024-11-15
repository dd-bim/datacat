package de.bentrm.datacat.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LanguageCodeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LanguageCodeConstraint {

    String message() default "Invalid language code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
