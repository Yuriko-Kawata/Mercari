package com.example.productmanagementex.form;

import jakarta.validation.constraints.NotBlank;

public class CategoryForm {
    // 親カテゴリ
    @NotBlank(message = "選択を変更してください")
    private String parentCategory;
    // 子カテゴリ
    @NotBlank(message = "選択を変更してください")
    private String childCategory;
    // 孫カテゴリ
    @NotBlank(message = "選択を変更してください")
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
