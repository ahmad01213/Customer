package com.UV.rokonalzorobyan.UV.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class curentDeatailItem {
    @SerializedName("item_key")
    @Expose
    private Integer itemKey;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_details")
    @Expose
    private String itemDetails;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_image")
    @Expose
    private String itemImage;

    public Integer getItemKey() {
        return itemKey;
    }

    public void setItemKey(Integer itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
