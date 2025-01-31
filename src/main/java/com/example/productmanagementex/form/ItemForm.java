package com.example.productmanagementex.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.productmanagementex.custom.DecimalRange;
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
    @NotBlank(message = "{error.empty}")
    @Size(max = 100, message = "{error.max}")
    private String name;
    // 状態
    @NotNull(message = "{error.empty}")
    private Integer condition;
    // カテゴリ
    private Integer category;
    // ブランド名
    @Size(max = 100, message = "{error.max}")
    private String brand;
    // 値段
    @NotNull(message = "{error.empty}")
    @DecimalRange(min = 0, max = 80000, message = "{error.numRange}")
    @Digits(integer = 5, fraction = 1, message = "{error.fraction}")
    private Double price;
    // 在庫
    private Integer stock;
    // 配送状態
    private Integer shipping;
    // 商品説明
    @NotBlank(message = "{error.empty}")
    @Size(max = 500, message = "{error.max}")
    private String description;
    // 状態（論理削除）
    private Integer delFlg;
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

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
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
                + ", description=" + description + ", delFlg=" + delFlg + ", image=" + image + "]";
    }

}
