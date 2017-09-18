package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * *******************************************
 * * Created by Simon on 18/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.fragProfile_tvUserName)
    EditText tvUserName;
    @BindView(R.id.fragProfile_tvDOB)
    EditText tvDOB;
    @BindView(R.id.fragProfile_tvEmail)
    EditText tvEmail;
    @BindView(R.id.fragProfile_tvMale)
    TextView tvMale;
    @BindView(R.id.fragProfile_tvFemale)
    TextView tvFemale;
    @BindView(R.id.fragProfile_tvAddress)
    EditText tvAddress;
    @BindView(R.id.fragProfile_tvMobilePhone)
    EditText tvMobilePhone;
    Unbinder unbinder;
    @BindView(R.id.fragProfile_ivMale)
    ImageView ivMale;
    @BindView(R.id.fragProfile_ivFemale)
    ImageView ivFemale;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setActionBar(view, getString(R.string.my_profile));
        setVisibilityTabBottom(View.GONE);
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

    @OnClick({R.id.fragProfile_llMale, R.id.fragProfile_llFemale, R.id.fragProfile_tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragProfile_llMale:
                handleSexSelected(true);
                break;
            case R.id.fragProfile_llFemale:
                handleSexSelected(false);
                break;
            case R.id.fragProfile_tvSubmit:
                break;
        }
    }
}
