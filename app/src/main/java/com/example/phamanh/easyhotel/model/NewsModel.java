package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class NewsModel extends BaseModel {

    private String title;
    private String linkImage;
    private String date;
    private String description;
    private String link;

    public String getTitle() {
        return title != null ? title : "";
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public String getLink() {
        return link != null ? link : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkImage() {
        return linkImage != null ? linkImage : "";
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }
}
