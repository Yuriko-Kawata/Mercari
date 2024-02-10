package com.example.productmanagementex.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * userのformクラス
 * 
 * @author hiraizumi
 */
public class UserForm {
    // 名前
    @NotBlank(message = "error.empty")
    @Size(max = 50, message = "{error.max}")
    private String name;
    // メールアドレス
    @NotBlank(message = "{error.empty}")
    @Email(message = "{error.mail.format}")
    @Size(max = 255, message = "{error.max}")
    private String mail;
    // パスワード
    @NotBlank(message = "error.empty")
    @Size(min = 8, max = 20, message = "{error.Range}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{0,}", message = "{error.password}")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{error.alphanumeric}")
    private String password;
    // 権限
    private Integer authority;
    // パスワードの再入力
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

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    @Override
    public String toString() {
        return "UserForm [name=" + name + ", mail=" + mail + ", password=" + password + ", authority=" + authority
                + ", passwordCheck=" + passwordCheck + "]";
    }

}
