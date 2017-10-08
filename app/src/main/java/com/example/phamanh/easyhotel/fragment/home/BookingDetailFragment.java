package com.example.phamanh.easyhotel.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.BookingModel;
import com.example.phamanh.easyhotel.model.EventBusBooking;
import com.example.phamanh.easyhotel.model.EventBusDateModel;
import com.example.phamanh.easyhotel.model.HistoryModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.other.database.DataHardCode;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.fragBookingDetail_tvNumber)
    EditText tvNumber;
    @BindView(R.id.fragBookingDetail_tvPrice)
    EditText tvPrice;
    @BindView(R.id.fragBookingDetail_tvNote)
    TextView tvNote;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Date mDate = new Date();
    private SelectSinglePopup popupRoom, popupService;
    private List<String> mDataRoom = new ArrayList<>();
    private String service;
    private boolean isCheckRoom;
    private InfomationModel mInfomationModel;
    private BookingModel mBookingModel;
    private int countRoom, numberRoom = 1, countDate;
    private double countPrice;

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
            tvRoomName.setText(isCheckRoom ? "Single" : "Double");
            tvNote.setVisibility(isCheckRoom ? View.GONE : View.VISIBLE);
            mInfomationModel = (InfomationModel) bundle.getSerializable(Constant.TITLE_INTRO);
            countPrice = isCheckRoom ? Double.parseDouble(mInfomationModel.getPrice()) : (1.5 * Double.parseDouble(mInfomationModel.getPrice()));
            countRoom = bundle.getInt(Constant.COMMENT);
        }
        tvPrice.setText(AppUtils.formatMoney(countPrice));
        tvHotelName.setText(mInfomationModel.getName());
        tvStartDate.setText(dateFormat.format(mDate));
        tvDueDate.setText(AppUtils.toInsertOneDay(tvStartDate.getText().toString()));
        tvEmail.setText(getUser().getEmail());
        tvFullName.setText(getUser().getFullName());
        tvPhone.setText(getUser().getPhone());
        tvService.setText(getArguments().getString(Constant.SERVICE));
        countDate = AppUtils.toCountDay(tvStartDate.getText().toString(), tvDueDate.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean toValidate() {
        boolean check = true;

        if (tvHotelName.getText().toString().trim().isEmpty()) {
            tvHotelName.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvRoomName.getText().toString().trim().isEmpty()) {
            tvRoomName.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvStartDate.getText().toString().trim().isEmpty()) {
            tvStartDate.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvDueDate.getText().toString().trim().isEmpty()) {
            tvDueDate.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvPersonal.getText().toString().trim().isEmpty()) {
            tvPersonal.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvFullName.getText().toString().trim().isEmpty()) {
            tvFullName.setHintTextColor(Color.RED);
            check = false;
        }
        if (tvPhone.getText().toString().trim().isEmpty()) {
            tvPhone.setHintTextColor(Color.RED);
            check = false;
        }

        return check;
    }

    private void toShowTextError() {
        String s = "";
        if (tvHotelName.getText().toString().trim().isEmpty()) {
            s = "Please input hotel name";
        } else if (tvRoomName.getText().toString().trim().isEmpty()) {
            s = "Please input room name";
        } else if (tvPersonal.getText().toString().trim().isEmpty()) {
            s = "Please input personal";
        } else if (tvFullName.getText().toString().trim().isEmpty()) {
            s = "Please input full name";
        } else if (tvPhone.getText().toString().trim().isEmpty()) {
            s = "Please input mobile phone";
        }
        if (!s.trim().isEmpty())
            toShowSnack(s);
    }

    private void toGetDataToPost() {
        mBookingModel = new BookingModel();
        mBookingModel.setIdBooking(String.valueOf(System.currentTimeMillis()));
        mBookingModel.setHotelName(tvHotelName.getText().toString());
        mBookingModel.setStartDate(tvStartDate.getText().toString());
        mBookingModel.setDueDate(tvDueDate.getText().toString());
        mBookingModel.setPersonal(tvPersonal.getText().toString());
        mBookingModel.setService(tvService.getText().toString());
        mBookingModel.setFullName(tvFullName.getText().toString());
        mBookingModel.setPhone(tvPhone.getText().toString());
        mBookingModel.setPersonal(tvPersonal.getText().toString());
        mBookingModel.setNumberRoom(String.valueOf(numberRoom));
        mBookingModel.setEmail(tvEmail.getText().toString());
        mBookingModel.setPrice(String.valueOf(numberRoom * countPrice));
        mBookingModel.setTypeRoom(tvRoomName.getText().toString());
        mBookingModel.setIdHotel(mInfomationModel.getId());
    }

    private void toShowSnack(String title) {
        AppUtils.showAlert(getContext(), title, null);
    }

    @OnClick({R.id.fragBookingDetail_tvStartDate, R.id.fragBookingDetail_tvDueDate, R.id.fragBookingDetail_tvService, R.id.fragBookingDetail_tvSubmit, R.id.fragBookingDetail_tvPersonal, R.id.fragBookingDetail_ivAdd, R.id.fragBookingDetail_ivSub})
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
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            if (toCheckRoom()) {
                                toGetDataToPost();
                                refMember_booking.child(mUser.getUid()).child(mBookingModel.getIdBooking()).setValue(new Gson().toJson(mBookingModel));
                                refHotel_booking.child(mInfomationModel.getId()).child(mBookingModel.getIdBooking()).setValue(new Gson().toJson(mBookingModel));
                                refHotel_room.child(mInfomationModel.getId()).child(isCheckRoom ? "single" : "_double").setValue(countRoom - numberRoom);
                                refMember_history.child(mUser.getUid()).child(mBookingModel.getIdBooking()).setValue(new Gson().toJson(new HistoryModel(Constant.MESS_BOOKING + mInfomationModel.getName(), System.currentTimeMillis(), mBookingModel)));
                                AppUtils.showAlert(getContext(), "Booking successful.", toChangeHome);
                                mInfomationModel = null;
                                EventBus.getDefault().postSticky(new EventBusBooking("pause"));
                            }
                            dismissLoading();
                        }
                    }.start();
                }
                break;
            case R.id.fragBookingDetail_tvPersonal:
                AppUtils.toGetPopup(getContext(), view, popupRoom, mDataRoom, tvPersonal);
                break;
            case R.id.fragBookingDetail_ivAdd:
                if (numberRoom < countRoom) {
                    numberRoom++;
                    tvNumber.setText(String.valueOf(numberRoom + (numberRoom == 1 ? " room" : " rooms")));
                    tvPrice.setText(AppUtils.formatMoney(numberRoom * countPrice * countDate));
                } else AppUtils.showAlert(getContext(), "Full room in the hotel.", null);
                break;
            case R.id.fragBookingDetail_ivSub:
                if (numberRoom > 1) {
                    numberRoom--;
                    tvNumber.setText(String.valueOf(numberRoom + (numberRoom == 1 ? " room" : " rooms")));
                    tvPrice.setText(AppUtils.formatMoney(numberRoom * countPrice * countDate));
                } else AppUtils.showAlert(getContext(), "Rooms are not empty.", null);
                break;
        }
    }

    private boolean toCheckRoom() {
        if (countRoom == 0) {
            AppUtils.showAlert(getContext(), "Out room.", new DialogListener() {
                @Override
                public void onConfirmClicked() {
                    getActivity().onBackPressed();
                }

                @Override
                public void onCancelClicked() {

                }
            });
            return false;
        }
        return true;
    }

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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBooking event) {
        if (event.getAction().equals(isCheckRoom ? "single" : "_double")) {
            countRoom = Integer.parseInt(event.getValue());
            if (numberRoom > countRoom) {
                AppUtils.showAlert(getContext(), countRoom != 0 ? "Now, maximum " + countRoom + " rooms in the hotel." : "Out room.", new DialogListener() {
                    @Override
                    public void onConfirmClicked() {
                        if (countRoom == 0)
                            getActivity().onBackPressed();
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
                numberRoom = countRoom;
                tvNumber.setText(String.valueOf(numberRoom + " room"));
                tvPrice.setText(AppUtils.formatMoney(numberRoom * countPrice * countDate));
            }
        }
        dismissLoading();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusDateModel event) {
        AppUtils.toCheckDate(getContext(), tvStartDate, tvDueDate);
        countDate = AppUtils.toCountDay(tvStartDate.getText().toString(), tvDueDate.getText().toString());
        tvPrice.setText(AppUtils.formatMoney(numberRoom * countPrice * countDate));
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
