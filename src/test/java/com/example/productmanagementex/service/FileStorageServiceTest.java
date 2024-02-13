package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class FileStorageServiceTest {

    @Mock
    private Path uploadDir;
    @InjectMocks
    private FileStorageService fileStorageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteFile() throws IOException {
        String filePath = "test";
        Path testPath = Paths.get(filePath);

        // FilesのdeleteIfExistsメソッドをモック化
        try (MockedStatic<Files> mocked = Mockito.mockStatic(Files.class)) {
            mocked.when(() -> Files.deleteIfExists(testPath)).thenReturn(true);

            fileStorageService.deleteFile(filePath);

            // deleteIfExistsが正しいパスで1回呼び出されたことを検証
            mocked.verify(() -> Files.deleteIfExists(testPath), times(1));
        }
    }

    @Test
    public void testDeleteFile_ThrowsIOException() {
        String filePath = "test";
        Path testPath = Paths.get(filePath);

        // FilesのdeleteIfExistsメソッドをモック化し、IOExceptionをスローするように設定
        try (MockedStatic<Files> mocked = Mockito.mockStatic(Files.class)) {
            mocked.when(() -> Files.deleteIfExists(testPath)).thenThrow(new IOException("Failed to delete file"));

            // 例外が投げられることを検証するための例外型を指定
            assertThrows(IllegalStateException.class, () -> {
                fileStorageService.deleteFile(filePath);
            });

            // deleteIfExistsが正しいパスで1回呼び出されたことを検証
            mocked.verify(() -> Files.deleteIfExists(testPath), times(1));
        }
    }
}
