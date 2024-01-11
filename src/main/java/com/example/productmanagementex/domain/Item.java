package com.example.productmanagementex.domain;

import java.util.List;

/**
 * 2023.12.27
 * 
 * @author hiraizumi
 *         itemのformクラス
 */
public class Item {
    // id
    private Integer id;
    // 商品名
    private String name;
    // 状態
    private Integer condition;
    // カテゴリ
    private List<Category> category;
    // ブランド名
    private String brand;
    // 値段
    private Double price;
    // 在庫
    private Integer stock;
    // 配送状態
    private Integer shipping;
    // 商品説明
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ItemForm [id=" + id + ", name=" + name + ", condition=" + condition + ", category=" + category
                + ", brand=" + brand + ", price=" + price + ", stock=" + stock + ", shipping=" + shipping
                + ", description=" + description + "]";
    }

}
