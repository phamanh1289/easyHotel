package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

/**
 * *******************************************
 * * Created by Simon on 18/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class FAQModel extends BaseModel {
    private String questions;
    private String answers;

    public FAQModel(String questions, String answers) {
        this.questions = questions;
        this.answers = answers;
    }

    public String getQuestions() {
        return questions;
    }

    public String getAnswers() {
        return answers;
    }
}
