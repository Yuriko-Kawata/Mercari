package com.example.productmanagementex.domain;

public class Category {
    private Integer id;
    private Integer parentId;
    private String name;
    private String nameAll;
    private Integer categoryNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String namel) {
        this.name = namel;
    }

    public String getNameAll() {
        return nameAll;
    }

    public void setNameAll(String nameAll) {
        this.nameAll = nameAll;
    }

    public Integer getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(Integer categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", parentId=" + parentId + ", name=" + name + ", nameAll=" + nameAll
                + ", categoryNumber=" + categoryNumber + "]";
    }
}
