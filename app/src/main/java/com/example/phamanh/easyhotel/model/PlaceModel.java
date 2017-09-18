package com.example.phamanh.easyhotel.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nhat Hoang on 16/03/2017.
 */

public class PlaceModel {
    private String mTitle;
    private LatLng latLng;

    public PlaceModel() {
    }

    public PlaceModel(String mTitle, LatLng latLng) {
        this.mTitle = mTitle;
        this.latLng = latLng;
    }

    public String getmTitle() {
        return mTitle == null ? "" : mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
