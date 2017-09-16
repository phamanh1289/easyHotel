package com.example.phamanh.easyhotel.base;

import android.app.Application;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.other.enums.RoleEnum;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class BaseApplication extends Application {
    public int currentTab;
    private RoleEnum role = RoleEnum.USER;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_Helvetica))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
