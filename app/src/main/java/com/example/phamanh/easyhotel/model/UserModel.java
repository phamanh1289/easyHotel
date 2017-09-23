package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class UserModel extends BaseModel {
    String email, gender, fullName, dob, address, phone, avatar;

    public UserModel(String email, String gender, String fullName, String dob, String address, String phone, String avatar) {
        this.email = email;
        this.gender = gender;
        this.fullName = fullName;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.avatar = avatar;
    }

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }
}
