package com.example.phamanh.easyhotel.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseActivity;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.SharedPrefUtils;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;



public class AuthActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        CountDownTimer fCountDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                toChangeActivity();
            }
        };
        fCountDownTimer.start();
    }

    private void toChangeActivity() {
        if (SharedPrefUtils.getString(this, Constant.TYPE_LOGIN).equals(Constant.LOGIN_NORMAL) || SharedPrefUtils.getString(this, Constant.TYPE_LOGIN).equals(Constant.LOGIN_SOCIAL)) {
            StartActivityUtils.toMain(this, null);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else{
            StartActivityUtils.toIntro(this);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            AppUtils.hidenStatusBar(this, Color.TRANSPARENT, true);
            fm.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
