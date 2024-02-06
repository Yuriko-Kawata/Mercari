package com.example.productmanagementex.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * カスタムValidationの作成
 * 
 * @author hiraizumi
 */
@Documented
@Constraint(validatedBy = CustomDecimalValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalRange {
    String message() default "価格の範囲が不正です";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    double min() default Double.MIN_VALUE;

    double max() default Double.MAX_VALUE;
}