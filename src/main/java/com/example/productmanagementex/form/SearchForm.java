package com.example.productmanagementex.form;

import jakarta.validation.constraints.Size;

/**
 * 検索用のformクラス
 * 
 * @author hiraizumi
 */
public class SearchForm {
    // 商品名
    @Size(max = 255, message = "{error.max}")
    private String name;
    // ブランド名
    @Size(max = 255, message = "{error.max}")
    private String brand;
    // 親カテゴリ（１次）
    private String parentCategory;
    // 子カテゴリ（２次）
    private String childCategory;
    // 孫カテゴリ（３次）
    private String grandCategory;
    // ソート要素
    private String sort;
    // ソート順序
    private String order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "SearchForm [name=" + name + ", brand=" + brand + ", parentCategory=" + parentCategory
                + ", childCategory=" + childCategory + ", grandCategory=" + grandCategory + ", sort=" + sort
                + ", order=" + order + "]";
    }
}
