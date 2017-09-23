package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

public class Location extends BaseModel {
    private String lat, lng;

    public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
