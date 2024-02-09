package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.repository.UserRepository;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRegisterUserAndFindUserByMail() {
        // テストデータの準備
        UserForm form = new UserForm();
        form.setName("Test Name");
        form.setMail("test@email.com");
        form.setPassword("password");

        // ユーザー登録処理の実行
        userService.registerUser(form);

        // データベースからユーザーを検索
        User user = userRepository.findUserByMail(form.getMail());

        // 検証
        assertEquals("Test Name", user.getName());
        assertEquals(null, userRepository.findUserByMail("not-found"));
        // パスワードなど他のフィールドも検証
    }

}
