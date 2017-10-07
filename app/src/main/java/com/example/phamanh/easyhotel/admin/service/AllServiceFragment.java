package com.example.phamanh.easyhotel.admin.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;


public class AllServiceFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_service, container, false);
        setActionBar(view, "All service");
        KeyboardUtils.setupUI(view, getActivity());
        init();
        return view;
    }

    private void init() {
    }

}
