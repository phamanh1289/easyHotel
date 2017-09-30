package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class RatingModel extends BaseModel {
    private String email;
    private String score;
    private Long time;

    public RatingModel(String email, String score, Long time) {
        this.email = email;
        this.score = score;
        this.time = time;
    }

    public RatingModel() {
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
