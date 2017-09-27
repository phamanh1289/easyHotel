package com.example.phamanh.easyhotel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {

    private int mData;
    private boolean isCheck;
    private ItemListener mListener;

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    public RoomAdapter(int data) {
        mData = data;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomHolder holder, int position) {
        holder.ivRoom.setImageResource(isCheck ? R.drawable.ic_signle_room_ok : R.drawable.ic_double_room_ok);
    }

    @Override
    public int getItemCount() {
        return mData != 0 ? mData : 0;
    }

    class RoomHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemRoom_ivRoom)
        ImageView ivRoom;

        public RoomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> mListener.onItemClicked(getAdapterPosition()));
        }
    }
}
