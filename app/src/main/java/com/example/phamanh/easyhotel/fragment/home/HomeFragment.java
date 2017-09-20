package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.HotelMainAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;
    @BindView(R.id.fragHome_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;

    private HotelModel model;
    private List<HotelModel> mDataHotel = new ArrayList<>();
    private HotelMainAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        KeyboardUtils.hideSoftKeyboard(getActivity());
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home));
        unbinder = ButterKnife.bind(this, view);
        ivBack.setVisibility(View.GONE);
        init();
        return view;
    }

    private void init() {
        adapter = new HotelMainAdapter(mDataHotel);
        adapter.setItemListener(toClickItem);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        toAddData();
    }

    private void toAddData() {
        if (mDataHotel.size() != 0)
            mDataHotel.clear();
        mDataHotel.add(new HotelModel("Đệ Nhất", "123/3 CMT8", getString(R.string.descrip_hotel_demo), R.drawable.ic_demo_1));
        mDataHotel.add(new HotelModel("Mường Thanh", "45 Lê Văn Sỹ", "", R.drawable.ic_demo_2));
        mDataHotel.add(new HotelModel("While Place", "112 Lê Đại Hành", "", R.drawable.ic_demo_3));
        adapter.notifyDataSetChanged();
    }

    ItemListener toClickItem = pos -> addFragment(HomeDetailFragment.newInstance(mDataHotel.get(pos)), true);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
