package com.example.productmanagementex.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * itemのformクラス
 * 
 * @author hiraizumi
 */
public class ItemForm {
    // id
    private Integer id;
    // 商品名
    @NotBlank(message = "入力は必須です")
    @Size(max = 100, message = "最大１００文字です")
    private String name;
    // 状態
    @NotNull(message = "選択は必須です")
    private Integer condition;
    // カテゴリ
    private Integer category;
    // ブランド名
    @Size(max = 100, message = "最大１００文字です")
    private String brand;
    // 値段
    @NotNull(message = "入力は必須です")
    @DecimalMin(value = "0", message = "価格は$0以上でなければなりません")
    @DecimalMax(value = "80000", message = "価格は$8000000以下でなければなりません")
    @Digits(integer = 5, fraction = 2, message = "価格は小数点第二位までで入力してください")
    private Double price;
    // 在庫
    private Integer stock;
    // 配送状態
    private Integer shipping;
    // 商品説明
    @NotBlank(message = "入力は必須です")
    @Size(max = 500, message = "最大５００文字です")
    private String description;
    // 状態（論理削除）
    private boolean delete;
    // image path
    private MultipartFile image;

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

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ItemForm [id=" + id + ", name=" + name + ", condition=" + condition + ", category=" + category
                + ", brand=" + brand + ", price=" + price + ", stock=" + stock + ", shipping=" + shipping
                + ", description=" + description + ", delete=" + delete + ", image=" + image + "]";
    }

}
