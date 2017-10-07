package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.io.Serializable;


public class RoomModel extends BaseModel implements Serializable{
    public String single, _double;

    public RoomModel(String single, String _double) {
        this.single = single;
        this._double = _double;
    }

    public RoomModel() {
    }
}
