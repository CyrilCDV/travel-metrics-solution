package org.cyril.travelmetricssolution.controller;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RawTravelDataArraysValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RawTravelDataArraysConstraint {
    String message() default "Arrays must not be null and must be of same size";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
