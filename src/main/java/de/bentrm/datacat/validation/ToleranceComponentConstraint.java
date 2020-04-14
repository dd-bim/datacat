package de.bentrm.datacat.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ToleranceComponentValidator.class)
@Target( { ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ToleranceComponentConstraint {

    String message() default "Invalid tolerance component";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
