package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class ServiceDetailModel extends BaseModel {
    private String nameService;

    public String getNameService() {
        return nameService;
    }

    public ServiceDetailModel(String nameService) {
        this.nameService = nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }
}
