package com.example.phamanh.easyhotel.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.HistoryModel;
import com.example.phamanh.easyhotel.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private List<HistoryModel> mData;
    private ItemListener mItemListener;

    public HistoryAdapter(List<HistoryModel> data) {
        mData = data;
    }

    public void setItemListener(ItemListener itemListener) {
        mItemListener = itemListener;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        HistoryModel item = mData.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvTime.setText((AppUtils.getTimeAgo(item.getTime())));
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemHistory_tvTitle)
        TextView tvTitle;
        @BindView(R.id.itemHistory_tvTime)
        TextView tvTime;

        public HistoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> mItemListener.onItemClicked(getAdapterPosition()));
        }
    }
}
