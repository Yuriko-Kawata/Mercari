package com.example.productmanagementex.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@code DecimalRange}アノテーションによって指定された範囲内であるかどうかを検証するためのカスタムバリデータ
 * このバリデータは、指定された{@code Double}値が特定の最小値と最大値の範囲内にあるかどうかを検証
 * 
 * @author hiraizumi
 */
public class CustomDecimalValidator implements ConstraintValidator<DecimalRange, Double> {
    private double min;
    private double max;

    /**
     * {@code DecimalRange}アノテーションから最小値と最大値のパラメータを取得して初期化
     * 
     * @param constraintAnnotation 検証対象のフィールドに適用された{@code DecimalRange}アノテーション
     */
    @Override
    public void initialize(DecimalRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    /**
     * 指定された{@code Double}値がこのバリデータによって設定された最小値と最大値の範囲内にあるかどうかを検証
     * 値が{@code null}の場合は検証をパス（{@code true}を返す）
     * 
     * @param value   検証する値
     * @param context バリデーションコンテキスト
     * @return 値が範囲内である場合は{@code true}、そうでない場合は{@code false}
     */
    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        // null値はチェックしない
        if (value == null) {
            return true;
        }
        return value >= min && value <= max;
    }
}
