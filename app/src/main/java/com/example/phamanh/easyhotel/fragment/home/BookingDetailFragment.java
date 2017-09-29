package com.example.phamanh.easyhotel.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.BookingModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.other.database.DataHardCode;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookingDetailFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.fragBookingDetail_tvStartDate)
    EditText tvStartDate;
    @BindView(R.id.fragBookingDetail_tvDueDate)
    EditText tvDueDate;
    @BindView(R.id.fragBookingDetail_tvService)
    EditText tvService;
    @BindView(R.id.fragBookingDetail_tvHotelName)
    EditText tvHotelName;
    @BindView(R.id.fragBookingDetail_tvRoomName)
    EditText tvRoomName;
    @BindView(R.id.fragBookingDetail_tvPersonal)
    EditText tvPersonal;
    @BindView(R.id.fragBookingDetail_tvFullName)
    EditText tvFullName;
    @BindView(R.id.fragBookingDetail_tvPhone)
    EditText tvPhone;
    @BindView(R.id.fragBookingDetail_tvEmail)
    EditText tvEmail;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Date mDate = new Date();
    private SelectSinglePopup popupRoom, popupService;
    private List<String> mDataRoom = new ArrayList<>();
    private String service;
    private boolean isCheckRoom;
    private InfomationModel mInfomationModel;
    private BookingModel mBookingModel;
    private int countRoom;

    public static BookingDetailFragment newInstance(InfomationModel title, String service, int room, boolean check, int count) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.TITLE_INTRO, title);
        args.putString(Constant.SERVICE, service);
        args.putInt(Constant.ID, room);
        args.putBoolean(Constant.KEY, check);
        args.putInt(Constant.COMMENT, count);
        BookingDetailFragment fragment = new BookingDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "Order Room");
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            isCheckRoom = bundle.getBoolean(Constant.KEY);
            mDataRoom.addAll(DataHardCode.getListRoom(isCheckRoom));
            mInfomationModel = (InfomationModel) bundle.getSerializable(Constant.TITLE_INTRO);
            countRoom = bundle.getInt(Constant.COMMENT);
        }
        tvHotelName.setText(mInfomationModel.getName());
        tvRoomName.setText("Room #" + String.valueOf(getArguments().getInt(Constant.ID)));
        tvStartDate.setText(dateFormat.format(mDate));
        tvDueDate.setText(dateFormat.format(mDate));
        tvEmail.setText(getUser().getEmail());
        tvFullName.setText(getUser().getFullName());
        tvPhone.setText(getUser().getPhone());
        tvService.setText(getArguments().getString(Constant.SERVICE));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean toValidate() {
        boolean check = true;

        if (tvHotelName.getText().toString().isEmpty()) {
            tvHotelName.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvRoomName.getText().toString().isEmpty()) {
            tvRoomName.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvStartDate.getText().toString().isEmpty()) {
            tvStartDate.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvDueDate.getText().toString().isEmpty()) {
            tvDueDate.setHintTextColor(Color.RED);
            check = false;
        }
        if (!AppUtils.toDoCheckDate(tvStartDate.getText().toString(), tvDueDate.getText().toString())) {
            AppUtils.showAlert(getContext(), "Error date.", toClickDialogCheckDate);
            check = false;
        }
        if (tvPersonal.getText().toString().isEmpty()) {
            tvPersonal.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvFullName.getText().toString().isEmpty()) {
            tvFullName.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvPhone.getText().toString().isEmpty()) {
            tvPhone.setHintTextColor(Color.RED);
            check = false;
        }

        return check;
    }

    private void toShowTextError() {
        String s = "";
        if (tvHotelName.getText().toString().isEmpty()) {
            s = "Please input hotel name";
        } else if (tvRoomName.getText().toString().isEmpty()) {
            s = "Please input room name";
        } else if (tvStartDate.getText().toString().isEmpty()) {
            s = "Please choice start date";
        } else if (tvDueDate.getText().toString().isEmpty()) {
            s = "Please choice due date";
        } else if (tvPersonal.getText().toString().isEmpty()) {
            s = "Please input personal";
        } else if (tvFullName.getText().toString().isEmpty()) {
            s = "Please input full name";
        } else if (tvPhone.getText().toString().isEmpty()) {
            s = "Please input mobile phone";
        }
        if (!s.isEmpty())
            toShowSnack(s);
    }

    private void toGetDataToPost() {
        mBookingModel = new BookingModel();
        mBookingModel.setHotelName(tvHotelName.getText().toString());
        mBookingModel.setRoomName(tvRoomName.getText().toString());
        mBookingModel.setStartDate(tvStartDate.getText().toString());
        mBookingModel.setDueDate(tvDueDate.getText().toString());
        mBookingModel.setPersonal(tvPersonal.getText().toString());
        mBookingModel.setService(tvService.getText().toString());
        mBookingModel.setFullName(tvFullName.getText().toString());
        mBookingModel.setPhone(tvPhone.getText().toString());
        mBookingModel.setEmail(tvEmail.getText().toString());
    }

    private void toShowSnack(String title) {
        AppUtils.showAlert(getContext(), title, null);
    }

    @OnClick({R.id.fragBookingDetail_tvStartDate, R.id.fragBookingDetail_tvDueDate, R.id.fragBookingDetail_tvService, R.id.fragBookingDetail_tvSubmit, R.id.fragBookingDetail_tvPersonal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragBookingDetail_tvStartDate:
                AppUtils.showPickTime(getContext(), tvStartDate, true);
                break;
            case R.id.fragBookingDetail_tvDueDate:
                AppUtils.showPickTime(getContext(), tvDueDate, true);
                break;
            case R.id.fragBookingDetail_tvSubmit:
                if (!toValidate())
                    toShowTextError();
                else {
                    showLoading();
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            toGetDataToPost();
                            refMember_booking.child(mUser.getUid()).push().setValue(new Gson().toJson(mBookingModel));
                            refHotel_room.child(mInfomationModel.getId()).child(isCheckRoom ? "single" : "double").setValue(countRoom - 1);
                            AppUtils.showAlert(getContext(), "Booking successful.", toChangeHome);
                            dismissLoading();
                        }
                    }.start();
                }
                break;
            case R.id.fragBookingDetail_tvPersonal:
                AppUtils.toGetPopup(getContext(), view, popupRoom, mDataRoom, tvPersonal);
                break;
        }
    }

    DialogListener toClickDialogCheckDate = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            AppUtils.showPickTime(getContext(), tvStartDate, true);
        }

        @Override
        public void onCancelClicked() {

        }
    };
    DialogListener toChangeHome = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            clearAllBackStack();
            StartActivityUtils.toMain(getContext(), null);
        }

        @Override
        public void onCancelClicked() {

        }
    };
}
