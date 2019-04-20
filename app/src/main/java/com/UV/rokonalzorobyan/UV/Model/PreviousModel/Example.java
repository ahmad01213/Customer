package com.UV.rokonalzorobyan.UV.Model.PreviousModel;
import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class Example {
    @SerializedName("key")
    @Expose
    private Integer key;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("whole_price")
    @Expose
    private String wholePrice;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("Delivery_cost")
    @Expose
    private String deliveryCost;
    @SerializedName("Rate")
    @Expose
    private Integer rate;
    @SerializedName("Rate_id")
    @Expose
    private Integer rateId;
    @SerializedName("Delivery_name")
    @Expose
    private String deliveryName;
    @SerializedName("Delivery_phone")
    @Expose
    private String deliveryPhone;
    @SerializedName("items")
    @Expose
    private List<List<Item>> items = null;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWholePrice() {
        return wholePrice;
    }

    public void setWholePrice(String wholePrice) {
        this.wholePrice = wholePrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public List<List<Item>> getItems() {
        return items;
    }

    public void setItems(List<List<Item>> items) {
        this.items = items;
    }

}