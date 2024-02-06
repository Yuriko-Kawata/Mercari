package com.example.productmanagementex.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * フィールドが指定された数値範囲内であることを検証するためのアノテーション
 * このアノテーションは、{@code double}型のフィールドに適用され、その値が指定された最小値と最大値の間にあることを保証
 * このアノテーションは、フィールド、メソッド、パラメータ、および他のアノテーションタイプに適用できます
 * バリデーションは{@link CustomDecimalValidator}によって行われる
 * 
 * @author hiraizumi
 */
@Documented
@Constraint(validatedBy = CustomDecimalValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalRange {
    // defaultメッセージ
    String message() default "価格の範囲が不正です";

    // バリデーションチェックのグループ化に用いるクラス
    Class<?>[] groups() default {};

    // カスタムペイロード
    Class<? extends Payload>[] payload() default {};

    // 最小値
    double min() default Double.MIN_VALUE;

    // 最大値
    double max() default Double.MAX_VALUE;
}