package com.example.phamanh.easyhotel.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.fragment.auth.IntroFragment;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;
import com.github.paolorotolo.appintro.AppIntro2;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(IntroFragment.newInstance("Easy search hotel.", R.drawable.ic_search));
        addSlide(IntroFragment.newInstance("Easy booking.", R.drawable.ic_booking));
        addSlide(IntroFragment.newInstance("Easy compare service.", R.drawable.ic_compare));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        StartActivityUtils.toLogin(this);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        StartActivityUtils.toLogin(this);
    }

    @Override
    public void onSlideChanged( Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
