package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderData {
    @SerializedName("Slider_key")
    @Expose
    private Integer sliderKey;
    @SerializedName("Slider_description")
    @Expose
    private String sliderDescription;
    @SerializedName("item_image")
    @Expose
    private String itemImage;

    public Integer getSliderKey() {
        return sliderKey;
    }

    public void setSliderKey(Integer sliderKey) {
        this.sliderKey = sliderKey;
    }

    public String getSliderDescription() {
        return sliderDescription;
    }

    public void setSliderDescription(String sliderDescription) {
        this.sliderDescription = sliderDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
