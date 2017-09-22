package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.util.ArrayList;
import java.util.List;


public class HotelModel extends BaseModel {
    private String id;
    public int room;
    public List<CommentModel> comment = new ArrayList<>();
    public List<RatingModel> rating = new ArrayList<>();
    public List<ServiceDetailModel> service;
    private InfomationModel infomation;

    public int getRoom() {
        return room;
    }

    public List<CommentModel> getDataComment() {
        return comment;
    }

    public List<RatingModel> getDataRating() {
        return rating;
    }


    public void setRoom(int room) {
        this.room = room;
    }

    public void setDataComment(List<CommentModel> dataComment) {
        comment = dataComment;
    }

    public void setDataRating(List<RatingModel> dataRating) {
        rating = dataRating;
    }

    public InfomationModel getInfomation() {
        return infomation;
    }

    public void setInfomation(InfomationModel infomation) {
        this.infomation = infomation;
    }

    public void setInfomationModel(InfomationModel infomationModel) {
        infomation = infomationModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InfomationModel getInfomationModel() {
        return infomation;
    }
}
