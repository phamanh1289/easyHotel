package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.BookingCommentPager;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.base.BaseModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BookingCommentParrent extends BaseFragment {

    @BindView(R.id.fragBookingComment_tvTitle)
    TextView tvTitle;
    @BindView(R.id.fragBookingComment_tlBookComment)
    TabLayout tabBookComment;
    @BindView(R.id.fragBookingComment_viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private BookingCommentPager adapter;
    public InfomationModel mInfomationModel;
    public ServiceDetailModel mServiceDetailModel;
    public String mKey;

    public static BookingCommentParrent newInstance(BaseModel item) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.BASE_MODEL, item);
        BookingCommentParrent fragment = new BookingCommentParrent();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_booking_comment, container, false);
        setActionBar(view, getString(R.string.page_home_detail));
        setVisibilityTabBottom(View.GONE);
        KeyboardUtils.setupUI(view, getActivity());
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mInfomationModel = (InfomationModel) bundle.getSerializable(Constant.BASE_MODEL);
            mKey = mInfomationModel.getId();
            adapter = new BookingCommentPager(getChildFragmentManager());
            viewpager.setAdapter(adapter);
            tabBookComment.setupWithViewPager(viewpager);
            tabBookComment.getTabAt(0).setText("Detail");
            tabBookComment.getTabAt(1).setText("Comment");
            tabBookComment.getTabAt(2).setText("Booking");
        }

        refHotel_service.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null && dataSnapshot.getKey().equals(mKey)) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                        if (jsonObject != null) {
                            mServiceDetailModel = gson.fromJson(jsonObject.toString(), ServiceDetailModel.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
        });

//        refHotel_service.child(mKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    try {
//                        Gson gson = new Gson();
//                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
//                        if (jsonObject != null) {
//                            mServiceDetailModel = gson.fromJson(jsonObject.toString(), ServiceDetailModel.class);
//                            mDataService.add(mServiceDetailModel);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
