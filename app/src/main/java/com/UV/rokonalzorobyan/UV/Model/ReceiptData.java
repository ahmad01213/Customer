package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiptData {
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("Delivery_cost")
    @Expose
    private String deliveryCost;
    @SerializedName("date")
    @Expose
    private String date;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
