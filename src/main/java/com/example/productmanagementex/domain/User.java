package com.example.productmanagementex.domain;

public class User {
    // ユーザーID
    private Integer id;
    // ユーザー名
    private String name;
    // メールアドレス
    private String mail;
    // パスワード
    private String password;
    // 作った人？
    private Integer authority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", mail=" + mail + ", password=" + password + ", authority="
                + authority + "]";
    }

}
