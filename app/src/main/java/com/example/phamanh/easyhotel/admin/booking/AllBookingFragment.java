package com.example.phamanh.easyhotel.admin.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.BookingModel;
import com.example.phamanh.easyhotel.model.EventBusMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AllBookingFragment extends BaseFragment {

    @BindView(R.id.fragBookingAdmin_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;

    private AllBookingAdapter adapter;
    private List<BookingModel> mDataBooking = new ArrayList<>();
    private List<String> mDataHotel = new ArrayList<>();
    private HashMap<String, BookingModel> mDataHashMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_admin, container, false);
        setActionBar(view, "all booking");
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        showLoading();
        adapter = new AllBookingAdapter(mDataBooking);
        adapter.setItemListener(toClick);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mDataHotel.size() != 0)
            mDataHotel.clear();
        refHotel_booking.addChildEventListener(toAddBooking);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessage data) {
        if (mDataBooking.size() != 0)
            mDataBooking.clear();
        for (int i = 0; i < mDataHashMap.values().size(); i++) {
            Gson gson = new Gson();
            mDataBooking.add(gson.fromJson((JsonElement) mDataHashMap.values().toArray()[i], BookingModel.class));
            adapter.notifyItemInserted(i);
        }
        dismissLoading();
    }

    private void toAddData() {
        if (mDataBooking.size() != 0)
            mDataBooking.clear();
        for (int i = 0; i < mDataHashMap.values().size(); i++) {
            try {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(mDataHashMap.values().toArray()[i].toString());
                mDataBooking.add(gson.fromJson(jsonObject.toString(), BookingModel.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyItemInserted(i);
        }
        dismissLoading();
    }

    ChildEventListener toAddBooking = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mDataHashMap.putAll((Map<? extends String, ? extends BookingModel>) dataSnapshot.getValue());
//            EventBus.getDefault().post(new EventBusMessage(0));
            toAddData();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ItemListener toClick = pos -> replaceFragment(BookingAdminDetailFragment.newInstance(mDataBooking.get(pos)), true);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
