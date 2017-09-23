package com.example.phamanh.easyhotel.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.RatingModel;
import com.example.phamanh.easyhotel.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingHolder> {

    private List<RatingModel> mData;
    private RatingModel item;


    public RatingAdapter(List<RatingModel> data) {
        mData = data;
    }

    @Override
    public RatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingHolder holder, int position) {
        item = mData.get(position);
        holder.tvDate.setText(AppUtils.convertTime(item.getTime()));
        holder.tvScore.setText(item.getScore());
        holder.tvUser.setText(item.getEmail());
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class RatingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemRating_tvDate)
        TextView tvDate;
        @BindView(R.id.itemRating_tvUserName)
        TextView tvUser;
        @BindView(R.id.itemRating_tvScore)
        TextView tvScore;

        public RatingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
