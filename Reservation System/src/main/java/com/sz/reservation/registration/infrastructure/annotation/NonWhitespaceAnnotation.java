package com.sz.reservation.registration.infrastructure.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=NonWhitespace.class)
public @interface NonWhitespaceAnnotation {
    String message() default "string cannot contain whitespaces";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
class NonWhitespace implements ConstraintValidator<NonWhitespaceAnnotation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.contains(" ");
    }
}