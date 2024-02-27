package com.example.productmanagementex.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Transactional
public class SHA256PasswordEncoderTest {
    @InjectMocks
    private SHA256PasswordEncoder sha256PasswordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEncode() {
        CharSequence rawPassword = "Qwerty1234";
        String expectedEncodedPassword = "PnC5SfpTmrEkT9xnFoeWVBwODY256SiSSVL+TeB1ERI=";

        assertEquals(expectedEncodedPassword, sha256PasswordEncoder.encode(rawPassword));
    }

    @Test
    void testMatches() {
        CharSequence rawPassword = "Qwerty1234";
        String expectedEncodedPassword = "PnC5SfpTmrEkT9xnFoeWVBwODY256SiSSVL+TeB1ERI=";

        boolean check = sha256PasswordEncoder.matches(rawPassword, expectedEncodedPassword);

        assertEquals(true, check);
    }
}
