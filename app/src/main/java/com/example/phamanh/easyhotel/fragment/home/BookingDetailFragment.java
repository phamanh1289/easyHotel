package com.example.phamanh.easyhotel.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.utils.AppUtils;

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

    public static BookingDetailFragment newInstance() {

        Bundle args = new Bundle();

        BookingDetailFragment fragment = new BookingDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
            AppUtils.showAlert(getContext(), getString(R.string.error), "Error date.", toClickDialogCheckDate);
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

    private void toShowSnack(String title) {
        Snackbar.make(mCoordinatorLayout,
                title, Snackbar.LENGTH_LONG)
                .setAction("Submit", v -> {
                }).show();
    }

    @OnClick({R.id.fragBookingDetail_tvStartDate, R.id.fragBookingDetail_tvDueDate, R.id.fragBookingDetail_tvService, R.id.fragBookingDetail_tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragBookingDetail_tvStartDate:
                AppUtils.showPickTime(getContext(), tvStartDate, true);
                break;
            case R.id.fragBookingDetail_tvDueDate:
                AppUtils.showPickTime(getContext(), tvDueDate, true);
                break;
            case R.id.fragBookingDetail_tvService:

                break;
            case R.id.fragBookingDetail_tvSubmit:
                if (!toValidate())
                    toShowTextError();
                else
                    Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
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
}
