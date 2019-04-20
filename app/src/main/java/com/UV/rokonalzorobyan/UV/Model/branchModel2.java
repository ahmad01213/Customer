package com.UV.rokonalzorobyan.UV.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class branchModel2 {
    @SerializedName("key")
    @Expose
    private Integer key;
    @SerializedName("Branch_name")
    @Expose
    private String branchName;
    @SerializedName("Long")
    @Expose
    private String _long;
    @SerializedName("Lat")
    @Expose
    private String lat;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}