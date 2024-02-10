package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.productmanagementex.repository.ImageRepository;

@SpringBootTest
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStorage() {
        int itemId = 1;
        String path = "/path/to/image.jpg";

        // Call the method under test
        imageService.storage(itemId, path);

        // Verify that the repository's insert method was called with the correct
        // arguments
        verify(imageRepository, times(1)).insert(itemId, path);
    }

    @Test
    public void testGetPathWithExistingPath() {
        int itemId = 1;
        String expectedPath = "/path/to/image.jpg";

        // Configure the mock to return the expected path when findPathByItemId is
        // called
        when(imageRepository.findPathByItemId(itemId)).thenReturn(expectedPath);

        // Call the method under test
        String actualPath = imageService.getPath(itemId);

        // Assert that the returned path matches the expected path
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testGetPathWithoutExistingPath() {
        int itemId = 2;

        // Configure the mock to return null when findPathByItemId is called
        when(imageRepository.findPathByItemId(itemId)).thenReturn(null);

        // Call the method under test
        String actualPath = imageService.getPath(itemId);

        // Assert that the returned path is the default no-image path
        assertEquals("/uploaded-img/no-image.png", actualPath);
    }

    @Test
    public void testUpdatePathWithExistingPath() {
        int itemId = 1;
        String newPath = "/new/path/to/image.jpg";

        // Configure the mock to return a non-null value when findPathByItemId is called
        when(imageRepository.findPathByItemId(itemId)).thenReturn("/old/path/to/image.jpg");

        // Call the method under test
        imageService.updatePath(itemId, newPath);

        // Verify that the repository's updatePath method was called with the correct
        // arguments
        verify(imageRepository, times(1)).updatePath(itemId, newPath);
    }

    @Test
    public void testUpdatePathWithoutExistingPath() {
        int itemId = 2;
        String newPath = "/new/path/to/image.jpg";

        // Configure the mock to return null when findPathByItemId is called
        when(imageRepository.findPathByItemId(itemId)).thenReturn(null);

        // Call the method under test
        imageService.updatePath(itemId, newPath);

        // Verify that the repository's insert method was called with the correct
        // arguments
        verify(imageRepository, times(1)).insert(itemId, newPath);
    }
}
