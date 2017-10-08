package com.example.phamanh.easyhotel.admin.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.BookingModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AllBookingFragment extends BaseFragment {

    @BindView(R.id.fragBookingAdmin_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;
    @BindView(R.id.fragBookingAdmin_tvAll)
    EditText tvAll;
    @BindView(R.id.fragBookingAdmin_tvEnable)
    EditText tvEnable;
    @BindView(R.id.fragBookingAdmin_tvDisable)
    EditText tvDisable;

    private AllBookingAdapter adapter;
    private List<BookingModel> mDataBooking = new ArrayList<>();
    private List<BookingModel> mDataSearch = new ArrayList<>();
    private HashMap<String, BookingModel> mDataHashMap = new HashMap<>();
    private boolean isCheckLoadData;

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
        toAddView(mDataBooking);
        if (mDataBooking.size() != 0)
            mDataBooking.clear();
        refHotel_booking.addChildEventListener(toAddBooking);

    }

    private void toAddView(List<BookingModel> mDataBooking) {
        adapter = new AllBookingAdapter(mDataBooking);
        adapter.setItemListener(toClick);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            refHotel_booking.removeEventListener(toAddBooking);
        } catch (Exception ignored) {
        }
    }

    private void toAddData() {
        for (int i = 0; i < mDataHashMap.values().size(); i++) {
            try {
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(mDataHashMap.values().toArray()[i].toString());
                mDataBooking.add(gson.fromJson(jsonObject.toString(), BookingModel.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
        dismissLoading();
    }

    ChildEventListener toAddBooking = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mDataHashMap.putAll((Map<? extends String, ? extends BookingModel>) dataSnapshot.getValue());
            toAddData();
            tvAll.setSelected(true);
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

    ItemListener toClick = pos -> replaceFragment(BookingAdminDetailFragment.newInstance(isCheckLoadData ? mDataSearch.get(pos) : mDataBooking.get(pos)), true);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void toChangeAction(EditText action) {
        tvAll.setSelected(false);
        tvEnable.setSelected(false);
        tvDisable.setSelected(false);
        action.setSelected(true);
    }

    @OnClick({R.id.fragBookingAdmin_tvAll, R.id.fragBookingAdmin_tvEnable, R.id.fragBookingAdmin_tvDisable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragBookingAdmin_tvAll:
                toChangeAction(tvAll);
                isCheckLoadData = false;
                toAddView(mDataBooking);
                break;
            case R.id.fragBookingAdmin_tvEnable:
                toChangeAction(tvEnable);
                isCheckLoadData = true;
                toSearchHotel(false, mDataBooking);
                toAddView(mDataSearch);
                break;
            case R.id.fragBookingAdmin_tvDisable:
                toChangeAction(tvDisable);
                isCheckLoadData = true;
                toSearchHotel(true, mDataBooking);
                toAddView(mDataSearch);
                break;
        }
    }

    private void toSearchHotel(boolean name, List<BookingModel> mDataInfo) {
        if (mDataSearch.size() != 0)
            mDataSearch.clear();
        for (BookingModel item : mDataInfo) {
            if (item.isCheckAction == name) {
                mDataSearch.add(item);
            }
        }
    }
}
