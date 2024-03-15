package com.example.productmanagementex.domain;

import java.util.Date;
import java.util.List;

/**
 * itemsのdomainクラス
 * 
 * @author hiraizumi
 */
public class Item {
    // id
    private Integer id;
    // 商品名
    private String name;
    // 状態
    private Integer condition;
    // カテゴリ
    private List<Category> categories;
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
    // カテゴリ名
    private Date updateTime;
    // 状態
    private Integer delFlg;

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", name=" + name + ", condition=" + condition + ", categories=" + categories + ", brand="
                + brand + ", price=" + price + ", stock=" + stock + ", shipping=" + shipping + ", description="
                + description + ", updateTime=" + updateTime + ", delFlg=" + delFlg + "]";
    }

}
