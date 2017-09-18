package com.example.phamanh.easyhotel.fragment.auth;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class LoginFragment extends BaseFragment {

    @BindView(R.id.fragLogin_tvLogin)
    TextView tvLogin;
    @BindView(R.id.fragLogin_etPassword)
    EditText etPassword;

    private boolean isShowPass;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        KeyboardUtils.setupUI(view, getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragLogin_ivShowPass, R.id.fragLogin_tvLogin, R.id.fragLogin_ivFacebook, R.id.fragLogin_ivGoogle, R.id.fragLogin_tvSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragLogin_ivShowPass:
                etPassword.setInputType(!isShowPass ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isShowPass = !isShowPass;
                break;
            case R.id.fragLogin_tvLogin:
                StartActivityUtils.toMain(getActivity(), null);
                break;
            case R.id.fragLogin_ivFacebook:
                break;
            case R.id.fragLogin_ivGoogle:
                break;
            case R.id.fragLogin_tvSignUp:
                addFragment(new SignUpFragment(), false);
                break;
        }
    }
}
