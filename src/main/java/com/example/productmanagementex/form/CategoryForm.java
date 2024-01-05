package com.example.productmanagementex.form;

public class CategoryForm {
    // 親カテゴリ（１次）
    private String parentCategory;
    // 子カテゴリ（２次）
    private String childCategory;
    // 孫カテゴリ（３次）
    private String grandCategory;

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

    @Override
    public String toString() {
        return "CategoryForm [parentCategory=" + parentCategory + ", childCategory=" + childCategory
                + ", grandCategory=" + grandCategory + "]";
    }

}
