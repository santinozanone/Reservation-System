package com.sz.reservation.registration.infrastructure.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NonWhitespaceAnnotationClass.class)
public @interface NotNullNotWhitespace {
    String message() default "string cannot contain whitespaces";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
