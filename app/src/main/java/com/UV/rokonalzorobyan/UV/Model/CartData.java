package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartData {
    @SerializedName("key")
    @Expose
    private Integer key;
    @SerializedName("items")
    @Expose
    private List<List<cartItem>> items = null;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
    public List<List<cartItem>> getItems() {
        return items;
    }

    public void setItems(List<List<cartItem>> items) {
        this.items = items;
    }
}
