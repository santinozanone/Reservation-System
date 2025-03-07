package com.sz.reservation.accountManagement.infrastructure.dto.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonWhitespaceAnnotationClass implements ConstraintValidator<NotNullNotWhitespace, String> {
    private final String WHITESPACE = " ";
    public NonWhitespaceAnnotationClass() {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.contains(WHITESPACE);
    }
}