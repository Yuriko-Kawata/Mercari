package com.example.productmanagementex.form;

import jakarta.validation.constraints.NotBlank;

public class CategoryForm {
    // ID
    private int id;
    // 名前
    private String name;
    // 親カテゴリID
    private int parentId;
    // 親カテゴリ
    @NotBlank(message = "選択を変更してください")
    private String parentCategory;
    // 子カテゴリ
    @NotBlank(message = "選択を変更してください")
    private String childCategory;
    // 孫カテゴリ
    @NotBlank(message = "選択を変更してください")
    private String grandCategory;
    // 全カテゴリ名
    private String nameAll;
    // カテゴリー番号
    private int categoryNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(String childCategory) {
        this.childCategory = childCategory;
    }

    public String getGrandCategory() {
        return grandCategory;
    }

    public void setGrandCategory(String grandCategory) {
        this.grandCategory = grandCategory;
    }

    public String getNameAll() {
        return nameAll;
    }

    public void setNameAll(String nameAll) {
        this.nameAll = nameAll;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    @Override
    public String toString() {
        return "CategoryForm [id=" + id + ", name=" + name + ", parentId=" + parentId + ", parentCategory="
                + parentCategory + ", childCategory=" + childCategory + ", grandCategory=" + grandCategory
                + ", nameAll=" + nameAll + ", categoryNumber=" + categoryNumber + "]";
    }

}
