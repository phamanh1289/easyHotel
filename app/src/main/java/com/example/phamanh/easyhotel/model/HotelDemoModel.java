package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class HotelDemoModel extends BaseModel {
    private String title, address, description;
    private int image;

    public HotelDemoModel(String title, String address, String description, int image) {
        this.title = title;
        this.address = address;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
