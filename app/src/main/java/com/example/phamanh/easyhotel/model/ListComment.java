package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

import java.util.List;

public class ListComment extends BaseModel {
    public List<CommentModel> comment;

    public ListComment(List<CommentModel> comment) {
        this.comment = comment;
    }

    public ListComment() {
    }
}
