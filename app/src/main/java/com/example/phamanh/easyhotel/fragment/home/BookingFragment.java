package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.RoomAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.RoomModel;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingFragment extends BaseFragment {


    @BindView(R.id.fragBooking_tvRoom)
    TextView tvRoom;
    @BindView(R.id.fragBooking_tvFormDate)
    EditText tvFormDate;
    @BindView(R.id.fragBooking_tvToDate)
    EditText tvToDate;
    @BindView(R.id.fragBooking_rvSingle)
    RecyclerView rvSingle;
    @BindView(R.id.fragBooking_rvDouble)
    RecyclerView rvDouble;
    @BindView(R.id.fragBooking_tvNoDataSingle)
    TextView tvNoDataSingle;
    @BindView(R.id.fragBooking_tvNoDataDouble)
    TextView tvNoDataDouble;

    private SelectSinglePopup popupRoom;
    private List<String> mDataRoom = new ArrayList<>();
    private RoomModel mRoomModel = new RoomModel();
    private RoomAdapter singleAdapter, doubleAdapter;
    private String mKey;
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
        mKey = ((BookingCommentParrent) getParentFragment()).mKey;
        refHotel_room.child(mKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRoomModel.single = Integer.parseInt(dataSnapshot.child("single").getValue().toString());
                if (singleAdapter == null) {
                    toSetUpUI(rvSingle, singleAdapter,mRoomModel.single, true);
                } else
                    singleAdapter.notifyDataSetChanged();
                tvNoDataSingle.setVisibility(mRoomModel.single != 0 ? View.GONE : View.VISIBLE);

                mRoomModel._double = Integer.parseInt(dataSnapshot.child("double").getValue().toString());
                if (doubleAdapter == null)
                    toSetUpUI(rvDouble, doubleAdapter,mRoomModel._double, false);
                else
                    doubleAdapter.notifyDataSetChanged();
                tvNoDataDouble.setVisibility(mRoomModel._double != 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void toSetUpUI(RecyclerView rvMain, RoomAdapter adapter, int i, boolean ischeck) {
        adapter = new RoomAdapter(i);
        adapter.setCheck(ischeck);
        rvMain.setAdapter(adapter);
//        rvMain.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen._3sdp)));
        rvMain.setLayoutManager(new GridLayoutManager(getActivity(), 5));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragBooking_tvFormDate, R.id.fragBooking_tvToDate, R.id.fragBooking_tvRoom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragBooking_tvFormDate:
                AppUtils.showPickTime(getContext(), tvFormDate, true);
                break;
            case R.id.fragBooking_tvToDate:
                AppUtils.showPickTime(getContext(), tvToDate, true);
                break;
            case R.id.fragBooking_tvRoom:
                AppUtils.toGetPopup(getContext(), view, popupRoom, mDataRoom, tvRoom);
                break;
//                if (!AppUtils.toDoCheckDate(tvFormDate.getText().toString(), tvToDate.getText().toString()))
//                    AppUtils.showAlert(getContext(), getString(R.string.error), "Failed date", toClickDialogCheckDate);
        }
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
}
