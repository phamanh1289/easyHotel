package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ServiceDetailModel extends BaseModel implements Serializable{
    private List<String> service = new ArrayList<>();

    public ServiceDetailModel(List<String> service) {
        this.service = service;
    }

    public ServiceDetailModel() {
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }
}
