package com.UV.rokonalzorobyan.UV.SliderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderData {
    @SerializedName("Slider_image")
    @Expose
    private String sliderImage;
    @SerializedName("android")
    @Expose
    private String android;

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }
}
