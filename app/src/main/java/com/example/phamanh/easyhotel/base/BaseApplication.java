package com.example.phamanh.easyhotel.base;

import android.app.Application;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.other.enums.RoleEnum;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class BaseApplication extends Application {
    public int currentTab;
    private UserModel user;
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

    public UserModel getUser() {
        return user;
    }

    public void setCustomer(UserModel model) {
        this.user = model;
    }
}
