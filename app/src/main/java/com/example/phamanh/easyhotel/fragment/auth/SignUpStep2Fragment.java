package com.example.phamanh.easyhotel.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

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

public class SignUpStep2Fragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.fragSignUp_etUserName)
    EditText etUserName;
    @BindView(R.id.fragSignUp_etDOB)
    EditText etDOB;
    @BindView(R.id.fragSingUp_etMobiPhone)
    EditText etMobiPhone;
    @BindView(R.id.fragSingUp_etAddress)
    EditText etAddress;
    @BindView(R.id.fragSignUp_ivMale)
    ImageView ivMale;
    @BindView(R.id.fragSignUp_tvMale)
    TextView tvMale;
    @BindView(R.id.fragSignUp_ivFemale)
    ImageView ivFemale;
    @BindView(R.id.fragSignUp_tvFemale)
    TextView tvFemale;

    public static SignUpStep2Fragment newInstance(String email, String password) {

        Bundle args = new Bundle();
        args.putString(Constant.SIGNUP_EMAIL, email);
        args.putString(Constant.SIGNUP_PASSWORD, password);
        SignUpStep2Fragment fragment = new SignUpStep2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_step_2, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        handleSexSelected(true);
    }


    private void handleSexSelected(boolean isMale) {
        ivMale.setSelected(isMale);
        tvMale.setSelected(isMale);
        ivFemale.setSelected(!isMale);
        tvFemale.setSelected(!isMale);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.fragSignUp_llMale, R.id.fragSignUp_llFemale, R.id.fragSignUp_tvLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragSignUp_llMale:
                handleSexSelected(true);
                break;
            case R.id.fragSignUp_llFemale:
                handleSexSelected(false);
                break;
            case R.id.fragSignUp_tvLogin:
                break;
        }
    }
}
