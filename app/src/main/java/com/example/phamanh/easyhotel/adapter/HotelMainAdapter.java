package com.example.phamanh.easyhotel.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.InfomationModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HotelMainAdapter extends RecyclerView.Adapter<HotelMainAdapter.HotelHolder> {

    private List<InfomationModel> mData;
    private ItemListener itemListener;
    private InfomationModel model;

    public HotelMainAdapter(List<InfomationModel> mData) {
        this.mData = mData;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public HotelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HotelHolder(view);
    }

    @Override
    public void onBindViewHolder(HotelHolder holder, int position) {
        model = mData.get(position);
        holder.tvAddress.setText(model.getAddress());
        holder.tvTitle.setText(model.getName());
        holder.ivBanner.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(model.getLogo(), Base64.DEFAULT), 0, Base64.decode(model.getLogo(), Base64.DEFAULT).length));
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class HotelHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemHome_tvTitle)
        TextView tvTitle;
        @BindView(R.id.itemHome_tvAddress)
        TextView tvAddress;
        @BindView(R.id.itemHome_ivBanner)
        ImageView ivBanner;

        public HotelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> itemListener.onItemClicked(getAdapterPosition()));
        }
    }
}
