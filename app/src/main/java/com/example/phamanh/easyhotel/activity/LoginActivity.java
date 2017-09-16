package com.example.phamanh.easyhotel.activity;

import android.os.Bundle;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseActivity;
import com.example.phamanh.easyhotel.fragment.auth.LoginFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment(new LoginFragment(), false);
    }
}

