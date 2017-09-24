package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.util.List;

public class ListRating extends BaseModel {
    public List<RatingModel> rating;

    public ListRating(List<RatingModel> rating) {
        this.rating = rating;
    }

    public ListRating() {
    }
}
