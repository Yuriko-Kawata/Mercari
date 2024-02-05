package com.example.productmanagementex.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomDecimalValidator implements ConstraintValidator<DecimalRange, Double> {
    private double min;
    private double max;

    @Override
    public void initialize(DecimalRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        // null値はチェックしない
        if (value == null) {
            return true;
        }
        return value >= min && value <= max;
    }
}
