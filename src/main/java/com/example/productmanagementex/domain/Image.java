package com.example.productmanagementex.domain;

/**
 * imageのdomainクラス
 * 
 * @author hiraizumi
 */
public class Image {
    // id
    private Integer id;
    // 対応するitem
    private Integer itemId;
    // image path
    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Image [id=" + id + ", itemId=" + itemId + ", path=" + path + "]";
    }

}
