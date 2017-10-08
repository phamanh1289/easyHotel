package com.example.phamanh.easyhotel.admin.booking;

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
import com.example.phamanh.easyhotel.model.BookingModel;
import com.example.phamanh.easyhotel.model.EventBusMessage;
import com.example.phamanh.easyhotel.model.HistoryModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookingAdminDetailFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.fragBookingAdminDetail_tvStartDate)
    EditText tvStartDate;
    @BindView(R.id.fragBookingAdminDetail_tvDueDate)
    EditText tvDueDate;
    @BindView(R.id.fragBookingAdminDetail_tvService)
    EditText tvService;
    @BindView(R.id.fragBookingAdminDetail_tvHotelName)
    EditText tvHotelName;
    @BindView(R.id.fragBookingAdminDetail_tvRoomName)
    EditText tvRoomName;
    @BindView(R.id.fragBookingAdminDetail_tvPersonal)
    EditText tvPersonal;
    @BindView(R.id.fragBookingAdminDetail_tvFullName)
    EditText tvFullName;
    @BindView(R.id.fragBookingAdminDetail_tvPhone)
    EditText tvPhone;
    @BindView(R.id.fragBookingAdminDetail_tvEmail)
    EditText tvEmail;
    @BindView(R.id.fragBookingAdminDetail_tvNumber)
    EditText tvNumber;
    @BindView(R.id.fragBookingAdminDetail_tvPrice)
    EditText tvPrice;
    @BindView(R.id.fragBookingAdminDetail_tvSubmit)
    TextView tvSubmit;

    private BookingModel mBookingModel;
    private int countRoom, oderRoom;
    private boolean isCheckRoom;
    private String type;

    public static BookingAdminDetailFragment newInstance(BookingModel item) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.BASE_MODEL, item);
        BookingAdminDetailFragment fragment = new BookingAdminDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_admin_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "Detail notification");
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBookingModel = (BookingModel) bundle.getSerializable(Constant.BASE_MODEL);
            type = mBookingModel.getTypeRoom().toLowerCase().equals("single") ? "single" : "_double";
            tvHotelName.setText(mBookingModel.getHotelName());
            tvRoomName.setText(mBookingModel.getTypeRoom());
            tvStartDate.setText(mBookingModel.getStartDate());
            tvDueDate.setText(mBookingModel.getDueDate());
            tvPersonal.setText(mBookingModel.getPersonal());
            tvService.setText(mBookingModel.getService());
            tvFullName.setText(mBookingModel.getFullName());
            tvPhone.setText(mBookingModel.getPhone());
            tvEmail.setText(mBookingModel.getEmail());
            oderRoom = Integer.parseInt(mBookingModel.getNumberRoom());
            tvNumber.setText(oderRoom + (oderRoom == 1 ? " room" : " rooms"));
            tvPrice.setText(AppUtils.formatMoney(Double.parseDouble(mBookingModel.getPrice())));
            tvSubmit.setVisibility(mBookingModel.isCheckAction ? View.GONE : View.VISIBLE);

            refHotel_room.child(mBookingModel.getIdHotel()).child(type).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String s = dataSnapshot.getValue().toString();
                    EventBus.getDefault().post(new EventBusMessage(Integer.parseInt(s)));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvet(EventBusMessage data) {
        countRoom = data.countRoom;
        EventBus.getDefault().removeAllStickyEvents();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragBookingAdminDetail_tvSubmit)
    public void onViewClicked() {
        AppUtils.showAlertConfirm(getContext(), "Do you want disable booking hotel", toDisable);
    }

    DialogListener toDisable = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            mBookingModel.isCheckAction = true;
            refMember_booking.child(mUser.getUid()).child(mBookingModel.getIdBooking()).setValue(new Gson().toJson(mBookingModel));
            refHotel_booking.child(mBookingModel.getIdHotel()).child(mBookingModel.getIdBooking()).setValue(new Gson().toJson(mBookingModel));
            refMember_history.child(mUser.getUid()).child(mBookingModel.getIdBooking()).setValue(new Gson().toJson(new HistoryModel(Constant.MESS_BOOKING_DISABLE + mBookingModel.getHotelName(), System.currentTimeMillis(), mBookingModel)));
            refHotel_room.child(mBookingModel.getIdHotel()).child(type).setValue(countRoom + oderRoom);
            tvSubmit.setVisibility(View.GONE);
            AppUtils.showAlert(getContext(), "Disable successful.", null);
        }

        @Override
        public void onCancelClicked() {

        }
    };
}
