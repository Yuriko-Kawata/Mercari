package com.example.productmanagementex.form;

/**
 * カテゴリーのformクラス
 * 
 * @author hiraizumi
 * 
 */
public class CategoryForm {
    // ID
    private int id;
    // 名前
    private String name;
    // 親カテゴリID
    private int parentId;
    // 親カテゴリ
    private String parentCategory;
    // 子カテゴリ
    private String childCategory;
    // 孫カテゴリ
    private String grandCategory;
    // 全カテゴリ名
    private String nameAll;

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

    @Override
    public String toString() {
        return "CategoryForm [id=" + id + ", name=" + name + ", parentId=" + parentId + ", parentCategory="
                + parentCategory + ", childCategory=" + childCategory + ", grandCategory=" + grandCategory
                + ", nameAll=" + nameAll + "]";
    }

}
