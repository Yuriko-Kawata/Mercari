package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        UserForm registerForm = new UserForm();
        registerForm.setName("Test Register");
        registerForm.setMail("test@example.com");
        registerForm.setPassword("plaintextpassword");
        UserForm userForm = new UserForm();
        userForm.setName("Test User");
        userForm.setMail("test2@example.com");
        userForm.setPassword("plaintextpassword");
        userForm.setAuthority(0);

        // Call the method under test
        userService.registerUser(registerForm);
        userService.registerUser(userForm);

        // Verify that the repository's insertUser method was called with the correct
        // arguments
        verify(userRepository, times(1)).insertUser(anyString(), eq(registerForm.getMail()), anyString(), eq(1));
        verify(userRepository, times(1)).insertUser(anyString(), eq(userForm.getMail()), anyString(), eq(0));
    }

    @Test
    public void testFindUserByMail() {
        String testEmail = "test@example.com";
        User expectedUser = new User();
        expectedUser.setMail(testEmail);

        // Configure the mock to return the expected user when findUserByMail is called
        when(userRepository.findUserByMail(testEmail)).thenReturn(expectedUser);

        // Call the method under test
        User actualUser = userService.findUserByMail(testEmail);

        // Assert that the returned user matches the expected user
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testHashPassword() {
        String plainTextPassword = "plaintextpassword";
        String hashedPassword = userService.hashPassword(plainTextPassword);

        // Assert that the hashed password is not null
        assertNotNull(hashedPassword);

        // Assert that the hashed password is not the same as the plain text password
        assertTrue(!hashedPassword.equals(plainTextPassword),
                "Hashed password should not be equal to plain text password");
    }
}
