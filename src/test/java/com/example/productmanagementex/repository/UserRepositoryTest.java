package com.example.productmanagementex.repository;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.User;

@SpringBootApplication
@Transactional
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @Mock
    private NamedParameterJdbcTemplate template;
    @InjectMocks
    private UserRepository repository;

    @Test
    void insertUser_ShouldInvokeUpdateWithCorrectParameters() {
        // Arrange
        String name = "TestName";
        String mail = "test@mail.com";
        String password = "password";
        int authority = 1;

        // Act
        repository.insertUser(name, mail, password, authority);
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void findUserByMail_UserExists_ReturnsUser() {
        // Arrange
        String mail = "user@example.com";
        User expectedUser = new User(); // 適切にUserオブジェクトを設定
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(expectedUser));

        // Act
        User result = repository.findUserByMail(mail);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void findUserByMail_UserDoesNotExist_ReturnsNull() {
        // Arrange
        String mail = "nonexistent@example.com";
        when(template.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Arrays.asList());

        // Act
        User result = repository.findUserByMail(mail);

        // Assert
        assertNull(result);
    }
}
