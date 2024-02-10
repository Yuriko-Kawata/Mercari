package com.example.productmanagementex.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.repository.CategoryRepository;
import com.example.productmanagementex.repository.ItemRepository;

@SpringBootTest
@Transactional
public class CategoryServiceTest {
    @Autowired
    private CategoryRepository repository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;

    @Test
    public void testFindAllCategory() {
        List<Category> categoryList = categoryService.findAllCategory();
    }
}
