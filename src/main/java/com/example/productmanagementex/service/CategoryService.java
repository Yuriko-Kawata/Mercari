package com.example.productmanagementex.service;

import java.util.List;
import java.util.ArrayList;

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

    public List<Category> findAllCategory() {
        List<Category> categoryList = repository.findAllCategory();
        return categoryList;
    }

    public int checkCategory(CategoryForm form) {
        if (form.getChildCategory() == null || form.getChildCategory() == "") {
            form.setChildCategory("カテゴリ無");
        }
        if (form.getGrandCategory() == null || form.getGrandCategory() == "") {
            form.setGrandCategory("カテゴリ無");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(form.getParentCategory());
        builder.append("/");
        builder.append(form.getChildCategory());
        builder.append("/");
        builder.append(form.getGrandCategory());
        String nameAll = builder.toString();

        int count = repository.checkCategory(nameAll);
        return count;
    }

    public int checkCategoryName(String name, int parentId, String nameAll) {
        int parentCondition = 0;
        if (parentId != 0) {
            if (nameAll == "") {
                parentCondition = 1;
            } else {
                parentCondition = 2;
            }
        }

        int count = repository.checkCategoryName(name, parentCondition);
        return count;
    }

    public void insertCategory(CategoryForm form) {
        if (form.getChildCategory() == null || form.getChildCategory() == "") {
            form.setChildCategory("カテゴリ無");
        }
        if (form.getGrandCategory() == null || form.getGrandCategory() == "") {
            form.setGrandCategory("カテゴリ無");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(form.getParentCategory());
        builder.append("/");
        builder.append(form.getChildCategory());
        builder.append("/");
        builder.append(form.getGrandCategory());
        String nameAll = builder.toString();

        repository.insertCategory(nameAll);
    }

    public void editCategoryNameAndNameAll(int id, String name, int parentId, String nameAll) {
        int parentCondition = 1;
        String originalName = repository.findNameById(id);
        String insertName = null;
        String insertOriginalName = null;
        String originalNameLike = null;
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder originalNameBuilder = new StringBuilder();

        if (parentId != 0) {
            if (nameAll == "") {
                parentCondition = 2;
                nameBuilder.append("/");
                nameBuilder.append(name);
                nameBuilder.append("/");
                insertName = nameBuilder.toString();

                originalNameBuilder.append("/");
                originalNameBuilder.append(originalName);
                originalNameBuilder.append("/");
                insertOriginalName = originalNameBuilder.toString();
                originalNameBuilder.insert(0, "%");
                originalNameBuilder.append("%");
                originalNameLike = originalNameBuilder.toString();
            } else {
                parentCondition = 3;
                nameBuilder.append("/");
                nameBuilder.append(name);
                insertName = nameBuilder.toString();

                originalNameBuilder.append("/");
                originalNameBuilder.append(originalName);
                insertOriginalName = originalNameBuilder.toString();
                originalNameBuilder.insert(0, "%");
                originalNameLike = originalNameBuilder.toString();

            }
        } else {
            nameBuilder.append(name);
            nameBuilder.append("/");
            insertName = nameBuilder.toString();

            originalNameBuilder.append(originalName);
            originalNameBuilder.append("/");
            insertOriginalName = originalNameBuilder.toString();
            originalNameBuilder.append("%");
            originalNameLike = originalNameBuilder.toString();
        }

        repository.editCategoryName(id, name, parentCondition);
        repository.editCategoryNameAll(id, insertName, insertOriginalName, originalNameLike, parentCondition);
    }

    public List<Category> findAllParentCategory(int page) {
        List<Category> categoryList = repository.findAllParentCategory(page);
        return categoryList;
    }

    public List<Category> findAllChildCategory(int page) {
        List<Category> categoryList = repository.findAllChildCategory(page);
        return categoryList;
    }

    public List<Category> findAllGrandCategory(int page) {
        List<Category> categoryList = repository.findAllGrandCategory(page);
        return categoryList;
    }

    public int totalParentPage() {
        int totalSize = repository.parentListSize();
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    public int totalChildPage() {
        int totalSize = repository.childListSize();
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    public int totalGrandPage() {
        int totalSize = repository.grandListSize();
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    public List<Category> searchParentCategory(String searchCondition, int page) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String condition = builder.toString();

        List<Category> categoryList = repository.searchParentCategory(condition, page);
        return categoryList;
    }

    public List<Category> searchChildCategory(String searchCondition, int page) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String condition = builder.toString();

        List<Category> categoryList = repository.searchChildCategory(condition, page);
        return categoryList;
    }

    public List<Category> searchGrandCategory(String searchCondition, int page) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String condition = builder.toString();

        List<Category> categoryList = repository.searchGrandCategory(condition, page);
        return categoryList;
    }

    public int searchParentTotalPage(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String condition = builder.toString();

        int totalSize = repository.searchParentTotalPage(condition);
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    public int searchChildTotalPage(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String condition = builder.toString();

        int totalSize = repository.searchChildTotalPage(condition);
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    public int searchGrandTotalPage(String searchCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("%");
        builder.append(searchCondition);
        builder.append("%");
        String condition = builder.toString();

        int totalSize = repository.searchGrandTotalPage(condition);
        int totalpage = totalSize / 30 + 1;
        return totalpage;
    }

    public Category findById(int id) {
        Category category = repository.findById(id);
        return category;
    }

    public int childCategoryCount(int id) {
        int count = repository.childCategorySize(id);
        return count;
    }

    public Category findParentCategory(int parentId) {
        Category category = repository.findParentCategory(parentId);
        return category;
    }

    public List<Category> findChildCategory(int id) {
        List<Category> categoryList = repository.findChildCategory(id);
        return categoryList;
    }

    public List<Integer> findChangeRecordId(int id, int parentId, String nameAll) {
        int parentCondition = 1;
        List<Integer> changeRecordId = new ArrayList<>();
        if (parentId != 0 && nameAll == "") {
            parentCondition = 2;
        } else if (parentId != 0 && nameAll != "") {
            changeRecordId.add(id);
            return changeRecordId;
        }
        changeRecordId = repository.findChangeRecordId(id, parentCondition);
        return changeRecordId;
    }

    public void delete(int id) {
        repository.delete(id);
    }

}
