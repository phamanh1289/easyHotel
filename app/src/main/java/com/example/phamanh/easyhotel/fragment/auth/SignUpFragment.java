package com.example.phamanh.easyhotel.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

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

public class SignUpFragment extends BaseFragment {

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        KeyboardUtils.setupUI(view,getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragSignUp_tvLogin, R.id.fragSignUp_ivFacebook, R.id.fragSignUp_ivGoogle, R.id.fragSignUp_tvSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragSignUp_tvLogin:
                break;
            case R.id.fragSignUp_ivFacebook:
                break;
            case R.id.fragSignUp_ivGoogle:
                break;
            case R.id.fragSignUp_tvSignUp:
                addFragment(new LoginFragment(),false);
                break;
        }
    }
}
