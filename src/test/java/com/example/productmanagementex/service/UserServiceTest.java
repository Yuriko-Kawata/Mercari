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
        UserForm authorityForm = new UserForm();
        authorityForm.setName("Test Name2");
        authorityForm.setMail("test2@email.com");
        authorityForm.setPassword("password");
        authorityForm.setAuthority(0);

        // ユーザー登録処理の実行
        userService.registerUser(form);
        userService.registerUser(authorityForm);

        // データベースからユーザーを検索
        User user = userService.findUserByMail(form.getMail());
        User authorityUser = userService.findUserByMail(authorityForm.getMail());

        // 検証
        assertEquals("Test Name", user.getName());
        assertEquals(1, user.getAuthority());
        assertEquals("Test Name2", authorityUser.getName());
        assertEquals(0, authorityUser.getAuthority());
        assertEquals(null, userRepository.findUserByMail("not-found"));
    }

}
