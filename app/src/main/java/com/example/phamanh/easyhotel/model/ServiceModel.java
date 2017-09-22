package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.util.ArrayList;
import java.util.List;


public class ServiceModel extends BaseModel {
    public List<ServiceDetailModel> mDataService = new ArrayList<>();

    public ServiceModel(List<ServiceDetailModel> dataService) {
        mDataService = dataService;
    }
}
