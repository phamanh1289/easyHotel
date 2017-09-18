package com.example.phamanh.easyhotel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.FAQModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * *******************************************
 * * Created by Simon on 09/08/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQHolder> {

    private List<FAQModel> mData;
    private ItemListener mItemListener;

    public void setItemListener(ItemListener itemListener) {
        mItemListener = itemListener;
    }

    public FAQAdapter(List<FAQModel> data) {
        mData = data;
    }

    @Override
    public FAQHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false);
        return new FAQHolder(view);
    }

    @Override
    public void onBindViewHolder(FAQHolder holder, int position) {
        FAQModel item = mData.get(position);
        holder.tvTitle.setText(item.getQuestions());
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class FAQHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemFAQ_tvTitle)
        TextView tvTitle;

        public FAQHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> mItemListener.onItemClicked(getAdapterPosition()));
        }
    }
}
