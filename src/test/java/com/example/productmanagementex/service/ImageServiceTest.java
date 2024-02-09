package com.example.productmanagementex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.repository.ImageRepository;

@SpringBootTest
@Transactional
public class ImageServiceTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageService imageService;

    @Test
    void testStorageAndGetPath() {
        int itemId = 999;
        String path = "test.png";

        // Call the method under test
        imageService.storage(itemId, path);

        assertEquals(path, imageService.getPath(itemId));
        assertEquals("/uploaded-img/no-image.png", imageService.getPath(0));
    }

    @Test
    void testUpdatePath() {
        int itemId = 1;
        int newItemId = 9999;
        String newPath = "test.png";

        imageService.updatePath(itemId, newPath);
        imageService.updatePath(newItemId, newPath);

        assertEquals(newPath, imageService.getPath(itemId));
        assertEquals(newPath, imageService.getPath(newItemId));
    }

}
