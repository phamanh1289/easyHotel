package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class RoomModel extends BaseModel {
    public int single, _double;

    public RoomModel(int single, int _double) {
        this.single = single;
        this._double = _double;
    }

    public RoomModel() {
    }
}
