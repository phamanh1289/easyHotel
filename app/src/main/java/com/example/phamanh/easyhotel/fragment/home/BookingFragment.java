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
import com.example.phamanh.easyhotel.model.EventBusBooking;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.RoomModel;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private boolean isCheckRoom;
    private int countSingle, countDouble;
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
                    mRoomModel.single = dataSnapshot.child("single").getValue().toString();
                    countSingle = Integer.parseInt(mRoomModel.single);
                    if (rvSingle != null) {
                        toSetUpUI(rvSingle, singleAdapter, toSingleClick, countSingle, true);
                    } else
                        singleAdapter.notifyDataSetChanged();
                    tvNoDataSingle.setVisibility(countSingle != 0 ? View.GONE : View.VISIBLE);
                    if (!isCheckRoom)
                        EventBus.getDefault().postSticky(new EventBusBooking("single", String.valueOf(mRoomModel.single)));

                    mRoomModel._double = dataSnapshot.child("_double").getValue().toString();
                    countDouble = Integer.parseInt(mRoomModel._double);
                    if (rvDouble != null) {
                        toSetUpUI(rvDouble, doubleAdapter, toDoubleClick, countDouble, false);
                    } else
                        doubleAdapter.notifyDataSetChanged();
                    tvNoDataDouble.setVisibility(countDouble != 0 ? View.GONE : View.VISIBLE);
                    if (!isCheckRoom)
                        EventBus.getDefault().postSticky(new EventBusBooking("_double", String.valueOf(mRoomModel._double)));
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
        EventBus.getDefault().unregister(this);
    }

    ItemListener toSingleClick = pos -> addFragment(BookingDetailFragment.newInstance(mInfomationModel, service, pos, true, countSingle), true);
    ItemListener toDoubleClick = pos -> addFragment(BookingDetailFragment.newInstance(((BookingCommentParrent) getParentFragment()).mInfomationModel, service, pos, false, countDouble), true);

    private void toSetUpUI(RecyclerView rvMain, RoomAdapter adapter, ItemListener toClick, int i, boolean isCheck) {
        adapter = new RoomAdapter(i);
        adapter.setCheck(isCheck);
        adapter.setListener(toClick);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBooking event) {
        if (event.getAction().equals("pause")) {
            isCheckRoom = true;
        }
        dismissLoading();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
