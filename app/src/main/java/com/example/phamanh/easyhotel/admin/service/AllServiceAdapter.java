package com.example.phamanh.easyhotel.admin.service;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AllServiceAdapter extends RecyclerView.Adapter<AllServiceAdapter.ServiceHolder> {

    private List<String> mData;
    private DialogListener mItemListener;

    public AllServiceAdapter(List<String> data) {
        mData = data;
    }

    public void setItemListener(DialogListener itemListener) {
        mItemListener = itemListener;
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class ServiceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemService_tvTitle)
        TextView tvTitle;

        public ServiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                mItemListener.onConfirmClicked();
                mItemListener.onCancelClicked();
            });
        }
    }
}
