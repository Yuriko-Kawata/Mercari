package com.example.productmanagementex.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserForm {
    // 名前
    @NotBlank(message = "入力は必須です")
    private String name;
    // メールアドレス
    @NotBlank(message = "入力は必須です")
    @Email(message = "形式が不正です")
    private String mail;
    // パスワード
    @NotBlank(message = "入力は必須です")
    @Size(min = 8, max = 16, message = "８文字以上１６文字以内で入力してください")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}", message = "英大文字、英小文字、数字を１文字以上含めてください")
    private String password;
    // パスワードの再入力
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$", message = "passwordの入力内容と一致しません")
    private String passwordCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    @Override
    public String toString() {
        return "UserForm [name=" + name + ", mail=" + mail + ", password=" + password + ", passwordCheck="
                + passwordCheck + "]";
    }

}
