package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;


public class HistoryModel extends BaseModel {
    private String title;
    private long time;
    private String content;
    private BookingModel discription;

    public HistoryModel(String title, long time, String contetn) {
        this.title = title;
        this.time = time;
        this.content = contetn;
    }
    public HistoryModel(String title, long time, BookingModel contetn) {
        this.title = title;
        this.time = time;
        this.discription = contetn;
    }

    public BookingModel getDiscription() {
        return discription;
    }

    public void setDiscription(BookingModel discription) {
        this.discription = discription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
