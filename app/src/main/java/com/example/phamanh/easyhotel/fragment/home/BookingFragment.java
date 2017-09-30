package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.RoomAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.RoomModel;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingFragment extends BaseFragment {


    @BindView(R.id.fragBooking_rvSingle)
    RecyclerView rvSingle;
    @BindView(R.id.fragBooking_rvDouble)
    RecyclerView rvDouble;
    @BindView(R.id.fragBooking_tvNoDataSingle)
    TextView tvNoDataSingle;
    @BindView(R.id.fragBooking_tvNoDataDouble)
    TextView tvNoDataDouble;


    private RoomModel mRoomModel = new RoomModel();
    private RoomAdapter singleAdapter, doubleAdapter;
    private InfomationModel mInfomationModel;
    private String service, mKey;
    private ServiceDetailModel mServiceDetailModel;
    private List<String> mDataService;
    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mInfomationModel = ((BookingCommentParrent) getParentFragment()).mInfomationModel;
        mKey = mInfomationModel.getId();

        refHotel_room.child(mKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mKey.equals("")) {
                    mRoomModel.single = Integer.parseInt(dataSnapshot.child("single").getValue().toString());
                    if (rvSingle != null) {
                        toSetUpUI(rvSingle, singleAdapter, toSingleClick, mRoomModel.single, true);
                    } else
                        singleAdapter.notifyDataSetChanged();
                    tvNoDataSingle.setVisibility(mRoomModel.single != 0 ? View.GONE : View.VISIBLE);

                    mRoomModel._double = Integer.parseInt(dataSnapshot.child("double").getValue().toString());
                    if (rvDouble != null) {
                        toSetUpUI(rvDouble, doubleAdapter, toDoubleClick, mRoomModel._double, false);
                    } else
                        doubleAdapter.notifyDataSetChanged();
                    tvNoDataDouble.setVisibility(mRoomModel._double != 0 ? View.GONE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mServiceDetailModel = ((BookingCommentParrent) getParentFragment()).mServiceDetailModel;
        mDataService = mServiceDetailModel.getService();
        int count = mDataService.size();
        for (int i = 0; i < count; i++) {
            if (i == 0)
                service = mDataService.get(i);
            else
                service += " - " + mDataService.get(i);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mInfomationModel = null;
        mKey = "";
    }

    ItemListener toSingleClick = pos -> addFragment(BookingDetailFragment.newInstance(mInfomationModel, service, pos, true, mRoomModel.single), true);
    ItemListener toDoubleClick = pos -> addFragment(BookingDetailFragment.newInstance(((BookingCommentParrent) getParentFragment()).mInfomationModel, service, pos, false, mRoomModel._double), true);

    private void toSetUpUI(RecyclerView rvMain, RoomAdapter adapter, ItemListener toClick, int i, boolean isCheck) {
        adapter = new RoomAdapter(i);
        adapter.setCheck(isCheck);
        adapter.setListener(toClick);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
