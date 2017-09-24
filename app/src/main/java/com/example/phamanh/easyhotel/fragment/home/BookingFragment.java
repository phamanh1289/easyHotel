package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.other.database.DataHardCode;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingFragment extends BaseFragment {


    @BindView(R.id.fragHomeDetail_tvRoom)
    TextView tvRoom;
    @BindView(R.id.fragHomeDetail_tvFormDate)
    EditText tvFormDate;
    @BindView(R.id.fragHomeDetail_tvToDate)
    EditText tvToDate;
    private SelectSinglePopup popupRoom;
    private List<String> mDataRoom = new ArrayList<>();
    private HotelModel mHotelModel;
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
//        mHotelModel = ((BookingCommentParrent) getParentFragment()).mHotelModel;
        mDataRoom.addAll(DataHardCode.getListRoom());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragHomeDetail_tvFormDate, R.id.fragHomeDetail_tvToDate, R.id.fragHomeDetail_tvRoom})
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
