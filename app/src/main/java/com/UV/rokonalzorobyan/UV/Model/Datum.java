package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {
    @SerializedName("category_key")
    @Expose
    private Integer categoryKey;
    @SerializedName("category _name")
    @Expose
    private String categoryName;
    @SerializedName("items")
    @Expose
    private List<List<Item>> items = null;

    public Integer getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(Integer categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<List<Item>> getItems() {
        return items;
    }

    public void setItems(List<List<Item>> items) {
        this.items = items;
    }
}
