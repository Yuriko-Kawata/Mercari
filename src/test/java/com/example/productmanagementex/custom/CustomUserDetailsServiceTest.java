package com.example.productmanagementex.custom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.productmanagementex.repository.UserRepository;

public class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUserFound_returnUserDetails() {
        // モックの設定
        com.example.productmanagementex.domain.User mockUser = new com.example.productmanagementex.domain.User();
        mockUser.setMail("test@example.com");
        mockUser.setPassword("hashedPassword");
        when(userRepository.findUserByMail("test@example.com")).thenReturn(mockUser);

        // メソッドの実行
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // 検証
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
    }

    @Test
    void whenUserNotFound_throwUsernameNotFoundException() {
        // モックの設定
        when(userRepository.findUserByMail("unknown@example.com")).thenReturn(null);

        // メソッドの実行と検証
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown@example.com");
        });
    }
}
