package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class LikeMemberModel extends BaseModel {
    private String hotel;

    public LikeMemberModel(String hotel) {
        this.hotel = hotel;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }
}
