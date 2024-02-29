package com.example.productmanagementex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.productmanagementex.domain.User;
import com.example.productmanagementex.form.UserForm;
import com.example.productmanagementex.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController controller;
    @Mock
    private UserService service;
    @Mock
    private MessageSource messageSource;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
    }

    @Test
    void toRegister_AddsUserFormToModel_AndReturnsRegisterView() {
        // Arrange
        UserForm form = new UserForm();

        // Act
        String viewName = controller.toRegister(form, model);

        // Assert
        verify(model, times(1)).addAttribute("userForm", form);
        assertEquals("register", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void registerUser_WithValidationErrors_ReturnsToRegisterView() {
        UserForm form = new UserForm();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = controller.registerUser(form, bindingResult, model);

        assertEquals("register", viewName);
        verify(model, never()).addAttribute(eq("passwordError"), any());
    }

    @SuppressWarnings("null")
    @Test
    void registerUser_WhenPasswordMismatch_ShouldReturnRegisterView() {
        // Arrange
        UserForm form = new UserForm(); // Set up your UserForm with mismatched passwords
        form.setPassword("password");
        form.setPasswordCheck("differentPassword");

        when(messageSource.getMessage(eq("error.mail.not.match"), any(), any(Locale.class)))
                .thenReturn("Passwords do not match");

        // Act
        String viewName = controller.registerUser(form, bindingResult, model);

        // Assert
        verify(model, times(1)).addAttribute(eq("passwordError"), anyString());
        assertEquals("register", viewName);
    }

    @Test
    void registerUser_SuccessfulRegistration_ReturnsConfirmView() {
        UserForm form = new UserForm();
        form.setPassword("password");
        form.setPasswordCheck("password");
        form.setMail("unique@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(service.findUserByMail(anyString())).thenReturn(null);

        String viewName = controller.registerUser(form, bindingResult, model);

        assertEquals("confirm/user-register-confirm", viewName);
        verify(service).registerUser(form);
    }

    @SuppressWarnings("null")
    @Test
    void registerUser_WhenEmailIsDuplicate_ShouldReturnRegisterViewWithErrors() {
        // Arrange
        UserForm form = new UserForm();
        form.setMail("duplicate@example.com");
        form.setPassword("password");
        form.setPasswordCheck("password");

        // Simulate that a user with the given email already exists
        when(service.findUserByMail(form.getMail())).thenReturn(new User()); // Adjust the return type as per your User
                                                                             // class

        // Setup the message source to return a specific error message for duplicate
        // email
        when(messageSource.getMessage(eq("error.mail.duplicate"), isNull(), eq(Locale.getDefault())))
                .thenReturn("Email is already in use");

        // Act
        String viewName = controller.registerUser(form, bindingResult, model);

        // Assert
        verify(model, times(1)).addAttribute(eq("mailError"), eq("Email is already in use"));
        verify(model, times(1)).addAttribute(eq("userForm"), eq(form));
        assertEquals("register", viewName);
    }
}
