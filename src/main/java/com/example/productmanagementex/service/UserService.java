package com.example.productmanagementex.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * ユーザーの新規作成
     * 
     * @param form form
     */
    public void registerUser(UserForm form) {
        logger.debug("Started registerUser");

        form.setPassword(hashPassword(form.getPassword()));

        // domainクラスへのコピー
        User user = new User();
        BeanUtils.copyProperties(form, user);
        // 管理者画面からの追加なら１
        if (user.getAuthority() == null) {
            user.setAuthority(1);
        }

        // 新規作成
        repository.insertUser(user.getName(), user.getMail(), user.getPassword(), user.getAuthority());
        logger.debug("Finished registerUser");
    }

    /**
     * メールアドレスが重複していないかチェック
     * 
     * @param mail mail
     * @return 一致するものがあれば１、なければ０
     */
    public User findUserByMail(String mail) {
        logger.debug("Started findUserbymail");

        User user = repository.findUserByMail(mail);

        logger.debug("Finished findUserbymail");
        return user;
    }

    /**
     * パスワードのハッシュ化（SHA-256とBase64使用）
     * 
     * @param password パスワード
     * @return ハッシュ化されたパスワード
     */
    protected String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
