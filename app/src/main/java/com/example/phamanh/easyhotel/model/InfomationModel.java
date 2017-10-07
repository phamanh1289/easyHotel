package com.example.phamanh.easyhotel.model;

import android.graphics.Bitmap;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class InfomationModel extends BaseModel implements Serializable{
    private String address, description, logo, name, price, id;
    public boolean isLike;
    private Bitmap bitmap;
    public List<String> mDataImage = new ArrayList<>();
    public Location location;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getDataImage() {
        return mDataImage;
    }

    public void setDataImage(List<String> dataImage) {
        mDataImage = dataImage;
    }

}
