package com.sz.reservation.registration.infrastructure.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonWhitespaceAnnotationClass implements ConstraintValidator<NotNullNotWhitespace, String> {
    public NonWhitespaceAnnotationClass() {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.contains(" ");
    }
}