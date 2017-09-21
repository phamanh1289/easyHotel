package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.base.BaseModel;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.other.database.DataHardCode;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

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

public class HomeDetailFragment extends BaseFragment {


    @BindView(R.id.fragHomeDetail_ivBanner)
    ImageView ivBanner;
    @BindView(R.id.fragHomeDetail_tvTitle)
    TextView tvTitle;
    @BindView(R.id.fragHomeDetail_tvDescription)
    TextView tvDescription;
    @BindView(R.id.fragHomeDetail_tvRoom)
    TextView tvRoom;
    @BindView(R.id.fragHomeDetail_tvFormDate)
    EditText tvFormDate;
    @BindView(R.id.fragHomeDetail_tvToDate)
    EditText tvToDate;
    @BindView(R.id.fragHomeDetail_tvNext)
    TextView tvNext;

    private SelectSinglePopup popupRoom;
    private List<String> mDataRoom = new ArrayList<>();
    Unbinder unbinder;

    public static HomeDetailFragment newInstance(BaseModel item) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.BASE_MODEL, item);
        HomeDetailFragment fragment = new HomeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home_detail));
        setVisibilityTabBottom(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            HotelModel item = (HotelModel) bundle.getSerializable(Constant.BASE_MODEL);
            if (item != null) {
                tvTitle.setText(item.getTitle());
                tvDescription.setText(item.getDescription());
                Glide.with(getContext()).load(item.getImage()).into(ivBanner);
            }
        }
        mDataRoom.addAll(DataHardCode.getListRoom());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragHomeDetail_tvFormDate, R.id.fragHomeDetail_tvToDate, R.id.fragHomeDetail_tvRoom, R.id.fragHomeDetail_tvNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragHomeDetail_tvFormDate:
                AppUtils.showPickTime(getContext(), tvFormDate, true);
                break;
            case R.id.fragHomeDetail_tvToDate:
                AppUtils.showPickTime(getContext(), tvToDate, true);
                break;
            case R.id.fragHomeDetail_tvRoom:
                AppUtils.toGetPopup(getContext(), view, popupRoom, mDataRoom, tvRoom);
                break;
            case R.id.fragHomeDetail_tvNext:
                if (!AppUtils.toDoCheckDate(tvFormDate.getText().toString(), tvToDate.getText().toString()))
                    AppUtils.showAlert(getContext(), getString(R.string.error), "Failed date", toClickDialogCheckDate);
                break;
        }
        if (toEnableNext())
            tvNext.setVisibility(View.VISIBLE);
    }

    DialogListener toClickDialogCheckDate = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            AppUtils.showPickTime(getContext(), tvFormDate, true);
        }

        @Override
        public void onCancelClicked() {

        }
    };

    private boolean toEnableNext() {
        return tvFormDate.getText().toString().length() != 0 || tvToDate.getText().toString().length() != 0 || tvRoom.getText().toString().length() != 0;
    }
}
