package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.repository.UserRepository;

public class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // Arrange
        UserForm form = new UserForm();
        form.setName("Test Name");
        form.setMail("test@email.com");
        form.setPassword("password");

        // サービスが行うハッシュ化プロセスを手動で行う
        String hashedPassword = hashPassword(form.getPassword());

        User expectedUser = new User();
        expectedUser.setName(form.getName());
        expectedUser.setMail(form.getMail());
        expectedUser.setPassword(hashedPassword); // ここでハッシュ化されたパスワードを使用
        expectedUser.setAuthority(1);

        // Act
        userService.registerUser(form);

        // Assert
        verify(repository, times(1)).insertUser(expectedUser.getName(), expectedUser.getMail(),
                expectedUser.getPassword(), expectedUser.getAuthority());
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // @Test
    // public void testRegisterUserThrowsNoSuchAlgorithmException() {
    // // Arrange
    // UserForm form = new UserForm();
    // form.setName("Test Name");
    // form.setMail("test@email.com");
    // form.setPassword("password");

    // // hashPasswordメソッドがNoSuchAlgorithmExceptionをスローするようにモック
    // doThrow(new RuntimeException("Error hashing
    // password")).when(userService).hashPassword(anyString());

    // // Act & Assert
    // assertThrows(RuntimeException.class, () -> userService.registerUser(form));
    // }

    @Test
    void testFindUserByMail() {
        // Arrange
        String email = "test@email.com";
        User expectedUser = new User();
        expectedUser.setMail(email);
        expectedUser.setName("Test User");
        // Set other fields as needed

        // Assume the repository returns the expected user when called with the given
        // email
        when(repository.findUserByMail(email)).thenReturn(expectedUser);

        // Act
        User foundUser = userService.findUserByMail(email);

        // Assert
        assertEquals(expectedUser, foundUser);
    }

}
