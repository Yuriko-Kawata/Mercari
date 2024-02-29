package com.example.productmanagementex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
    @Mock
    private Model model;

    @InjectMocks
    private LoginController controller;

    @Test
    void login_WithErrorParam_ShouldAddLoginErrorToModel() {
        // Arrange
        String error = "true";

        // Act
        String viewName = controller.login(error, model);

        // Assert
        verify(model, times(1)).addAttribute("loginError", true);
        assertEquals("login", viewName);
    }

    @SuppressWarnings("null")
    @Test
    void login_WithoutErrorParam_ShouldNotAddLoginErrorToModel() {
        // Act
        String viewName = controller.login(null, model);

        // Assert
        verify(model, never()).addAttribute(eq("loginError"), any());
        assertEquals("login", viewName);
    }
}
