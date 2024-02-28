package com.example.productmanagementex.custom;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@SpringBootApplication
@Transactional
public class CustomDecimalValidatorTest {

    private CustomDecimalValidator validator;
    private ConstraintValidatorContext context;
    DecimalRange constraintAnnotation = new DecimalRange() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return DecimalRange.class;
        }

        @Override
        public double min() {
            return 1.0;
        }

        @Override
        public double max() {
            return 100.0;
        }

        // 必要に応じてデフォルト値を返す
        @Override
        public Class<?>[] groups() {
            return new Class<?>[0]; // 空の配列を返す
        }

        @Override
        public String message() {
            return ""; // デフォルトのメッセージを返す
        }

        // バリデーションアノテーションにpayloadメソッドがある場合
        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends Payload>[] payload() {
            return new Class[0]; // 空の配列を返す
        }
    };

    @BeforeEach
    void setUp() {
        validator = new CustomDecimalValidator();

        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void testInitialize() {

        // メソッドをテスト
        validator.initialize(constraintAnnotation);

        // リフレクションを使用してプライベートフィールドにアクセス
        assertAll(
                () -> assertEquals(1.0, getField(validator, "min"), "Min値が正しく設定されていません。"),
                () -> assertEquals(100.0, getField(validator, "max"), "Max値が正しく設定されていません。"));
    }

    // ヘルパーメソッド: プライベートフィールドの値を取得
    private Object getField(Object object, String fieldName) throws ReflectiveOperationException {
        var field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    @Test
    void isValid_NullValue_ReturnsTrue() {
        validator.initialize(constraintAnnotation);
        assertTrue(validator.isValid(null, context), "Null値は常にtrueを返すべきです。");
    }

    @Test
    void isValid_ValueInRange_ReturnsTrue() {
        validator.initialize(constraintAnnotation);
        assertTrue(validator.isValid(50.0, context), "範囲内の値はtrueを返すべきです。");
    }

    @Test
    void isValid_ValueBelowRange_ReturnsFalse() {
        validator.initialize(constraintAnnotation);
        assertFalse(validator.isValid(0.5, context), "範囲より小さい値はfalseを返すべきです。");
    }

    @Test
    void isValid_ValueAboveRange_ReturnsFalse() {
        validator.initialize(constraintAnnotation);
        assertFalse(validator.isValid(101.0, context), "範囲より大きい値はfalseを返すべきです。");
    }
}
