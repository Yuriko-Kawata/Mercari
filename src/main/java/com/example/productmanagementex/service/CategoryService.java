package com.example.productmanagementex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.repository.CategoryRepository;

@Transactional
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAllUniqueCategory() {
        List<Category> categoryList = repository.findAllUniqueCategory();
        return categoryList;
    }
}
