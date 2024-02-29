package com.example.productmanagementex.repository;

import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Transactional
public class ImageRepositoryTest {
    @Mock
    private NamedParameterJdbcTemplate template;

    @InjectMocks
    private ImageRepository imageRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        int itemId = 1;
        String path = "path/to/image.jpg";

        // Call the method under test
        imageRepository.insert(itemId, path);
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void whenPathExists_thenReturnsPath() {
        // Arrange
        int itemId = 1;
        String expectedPath = "path/to/image.jpg";
        when(template.query(anyString(), any(SqlParameterSource.class), any(ResultSetExtractor.class)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<String> extractor = invocation.getArgument(2);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(true);
                    when(rs.getString("path")).thenReturn(expectedPath);
                    return extractor.extractData(rs);
                });

        // Act
        String actualPath = imageRepository.findPathByItemId(itemId);

        // Assert
        assertEquals(expectedPath, actualPath);
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    void whenPathDoesNotExist_thenReturnsNull() {
        // Arrange
        int itemId = 1;
        when(template.query(anyString(), any(SqlParameterSource.class), any(ResultSetExtractor.class)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<String> extractor = invocation.getArgument(2);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(false);
                    return extractor.extractData(rs);
                });

        // Act
        String actualPath = imageRepository.findPathByItemId(itemId);

        // Assert
        assertNull(actualPath);
    }

    @Test
    void testUpdatePath() {
        // Prepare the test data
        int itemId = 1;
        String path = "new/path/to/image.jpg";

        // Call the method under test
        imageRepository.updatePath(itemId, path);
    }
}
