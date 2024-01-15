package com.example.productmanagementex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagementex.domain.Category;
import com.example.productmanagementex.form.CategoryForm;
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

    public void checkCategory(CategoryForm form) {
        StringBuilder builder = new StringBuilder();
        builder.append(form.getParentCategory());
        builder.append("/");
        builder.append(form.getChildCategory());
        builder.append("/");
        builder.append(form.getGrandCategory());
        String nameAll = builder.toString();

        repository.checkCategory(form.getParentCategory(), form.getChildCategory(), form.getGrandCategory(), nameAll);
    }
}
