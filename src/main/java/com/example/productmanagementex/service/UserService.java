package com.example.productmanagementex.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.repository.UserRepository;

/**
 * userのserviceクラス
 * 
 * @author hiraizumi
 */
@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    /**
     * ユーザーの新規作成
     * 
     * @param form form
     */
    public void registerUser(UserForm form) {
        // パスワードのハッシュ化（SHA-256とBase64使用）
        try {
            // SHA-256メッセージダイジェストのインスタンスを取得
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // パスワードをバイト配列に変換してハッシュ化
            byte[] hashedBytes = md.digest(form.getPassword().getBytes(StandardCharsets.UTF_8));

            // Base64エンコーディングを使用して、バイト配列を文字列に変換
            form.setPassword(Base64.getEncoder().encodeToString(hashedBytes));
        } catch (NoSuchAlgorithmException e) {
            // SHA-256アルゴリズムが見つからない場合のエラー処理
            e.printStackTrace();
        }

        // domainクラスへのコピー
        User user = new User();
        BeanUtils.copyProperties(form, user);

        // 新規作成
        repository.insertUser(user.getName(), user.getMail(), user.getPassword());
    }

    /**
     * メールアドレスが重複していないかチェック
     * 
     * @param mail mail
     * @return 一致するものがあれば１、なければ０
     */
    public User findUserByMail(String mail) {
        User user = repository.findUserByMail(mail);
        return user;
    }

}
