package com.example.phamanh.easyhotel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class SelectionSingleAdapter extends RecyclerView.Adapter<SelectionSingleAdapter.SelectionSingleViewHolder> {

    private List<String> data;
    private ItemListener listener;

    public SelectionSingleAdapter(List<String> data) {
        this.data = data;
    }

    public void setOnItemListener(ItemListener listener) {
        this.listener = listener;
    }

    @Override
    public SelectionSingleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection_single, parent, false);
        return new SelectionSingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectionSingleViewHolder holder, int position) {
        holder.tvTitle.setText(data.get(position));
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class SelectionSingleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemSelection_tvTitle)
        TextView tvTitle;

        SelectionSingleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
