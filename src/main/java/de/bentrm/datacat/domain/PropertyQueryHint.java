package de.bentrm.datacat.domain;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface PropertyQueryHint {
	String alias() default "root";
	String[] value();
}
