package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.productmanagementex.repository.ImageRepository;

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
    void testStorage() {
        int itemId = 1;
        String path = "path/to/image.jpg";

        // Call the method under test
        imageService.storage(itemId, path);

        // Verify that the repository's insert method was called with the correct
        // arguments
        verify(imageRepository).insert(itemId, path);
    }

    @Test
    void testGetPath() {
        int itemId = 1;
        String expectedPath = "path/to/image.jpg";

        // Set up the mock to return a specific value
        when(imageRepository.findPathByItemId(itemId)).thenReturn(expectedPath);

        // Call the method under test
        String actualPath = imageService.getPath(itemId);

        // Assert that the returned path matches the expected value
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testGetPath_whenPathDoesNotExist() {
        int itemId = 1;

        // Set up the mock to return null
        when(imageRepository.findPathByItemId(itemId)).thenReturn(null);

        // Call the method under test
        String actualPath = imageService.getPath(itemId);

        // Assert that the default path is returned
        assertEquals("/uploaded-img/no-image.png", actualPath);
    }

    @Test
    void testUpdatePath() {
        int itemId = 1;
        String newPath = "new/path/to/image.jpg";

        // Set up the mock to return a specific value
        when(imageRepository.findPathByItemId(itemId)).thenReturn("old/path/to/image.jpg");

        // Call the method under test
        imageService.updatePath(itemId, newPath);

        // Verify that the repository's updatePath method was called with the correct
        // arguments
        verify(imageRepository).updatePath(itemId, newPath);
    }

    @Test
    public void testUpdatePath_whenPathDoesNotExist() {
        int itemId = 1;
        String newPath = "new/path/to/image.jpg";

        // Set up the mock to return null
        when(imageRepository.findPathByItemId(itemId)).thenReturn(null);

        // Call the method under test
        imageService.updatePath(itemId, newPath);

        // Verify that the repository's insert method was called with the correct
        // arguments
        verify(imageRepository).insert(itemId, newPath);
    }
}
