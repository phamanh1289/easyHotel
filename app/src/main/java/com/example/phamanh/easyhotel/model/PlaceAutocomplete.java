package com.example.phamanh.easyhotel.model;

import com.example.phamanh.easyhotel.base.BaseModel;

/**
 * *******************************************
 * * Created by Simon on 10/6/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class PlaceAutocomplete extends BaseModel {
    public CharSequence placeId;
    public CharSequence primary;
    public CharSequence fulltext;
    public CharSequence secondary;

    public PlaceAutocomplete(CharSequence placeId, CharSequence primary, CharSequence fulltext, CharSequence secondary) {
        this.primary = primary;
        this.placeId = placeId;
        this.fulltext = fulltext;
        this.secondary = secondary;
    }

    @Override
    public String toString() {
        return fulltext.toString();
    }
}
