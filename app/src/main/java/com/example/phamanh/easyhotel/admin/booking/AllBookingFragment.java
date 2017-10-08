package com.example.phamanh.easyhotel.admin.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;


public class AllBookingFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_admin,container,false);
        setActionBar(view,"all booking");
        return view;
    }
}
