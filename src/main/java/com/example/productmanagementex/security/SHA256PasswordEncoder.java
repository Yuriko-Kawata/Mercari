package com.example.productmanagementex.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * passsword encoder
 * 
 * @author hiraizumi
 */
public class SHA256PasswordEncoder implements PasswordEncoder {

    /**
     * 指定された生パスワードをSHA-256アルゴリズムでエンコードし、エンコードされたパスワードをbase64文字列として返す
     *
     * @param rawPassword エンコードする生パスワード
     * @return エンコードされたパスワードをbase64文字列として返す
     * @throws NoSuchAlgorithmException SHA-256アルゴリズムが環境で利用できない場合
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    rawPassword.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * 指定された生パスワードがエンコードされたパスワードと一致するかどうかをチェック
     *
     * @param rawPassword     チェックする生パスワード
     * @param encodedPassword 生パスワードと比較するエンコードされたパスワード
     * @return エンコードされた生パスワードが指定されたエンコードされたパスワードと一致する場合はtrueを、一致しない場合はfalseを返す
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
