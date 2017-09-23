package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class CommentModel extends BaseModel {
    private long time;
    private String email;
    private String content;
    private String image;

    public CommentModel(long time, String email, String content, String image) {
        this.time = time;
        this.email = email;
        this.content = content;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
