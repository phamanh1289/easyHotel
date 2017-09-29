package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.util.List;


public class ServiceDetailModel extends BaseModel {
    private List<String> service;

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
