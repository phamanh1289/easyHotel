package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

/**
 * *******************************************
 * * Created by Simon on 18/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class HotelModel extends BaseModel {
    private String title, address, description;
    private int image;

    public HotelModel(String title, String address, String description, int image) {
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
