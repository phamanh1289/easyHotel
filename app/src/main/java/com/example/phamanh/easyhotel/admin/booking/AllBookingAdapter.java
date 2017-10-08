package com.example.phamanh.easyhotel.admin.booking;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.BookingModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllBookingAdapter extends RecyclerView.Adapter<AllBookingAdapter.BookingHolder> {

    private List<BookingModel> mData;
    private ItemListener mItemListener;

    public AllBookingAdapter(List<BookingModel> data) {
        mData = data;
    }

    public void setItemListener(ItemListener itemListener) {
        mItemListener = itemListener;
    }

    @Override
    public BookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_admin, parent, false);
        return new BookingHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingHolder holder, int position) {
        BookingModel model = mData.get(position);
        holder.tvEmail.setText(model.getFullName());
        holder.tvHotelName.setText(model.getHotelName());
        holder.tvStart.setText(model.getStartDate());
        holder.tvDue.setText(model.getDueDate());
        holder.ivBanner.setImageResource(model.getTypeRoom().toLowerCase().equals("single") ? R.drawable.ic_signle_room_ok : R.drawable.ic_double_room_ok);
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class BookingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fragBookingAdmin_tvEmail)
        EditText tvEmail;
        @BindView(R.id.fragBookingAdmin_tvHotelName)
        EditText tvHotelName;
        @BindView(R.id.fragBookingAdmin_tvStarDate)
        EditText tvStart;
        @BindView(R.id.fragBookingAdmin_tvDueDate)
        EditText tvDue;
        @BindView(R.id.fragBookingAdmin_ivBanner)
        ImageView ivBanner;

        public BookingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                mItemListener.onItemClicked(getAdapterPosition());
            });
        }
    }
}
