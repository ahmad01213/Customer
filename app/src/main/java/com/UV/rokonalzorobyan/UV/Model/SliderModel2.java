package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderModel2 {
    @SerializedName("Slider_image")
    @Expose
    private String sliderImage;

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }
}
