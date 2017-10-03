package com.example.phamanh.easyhotel.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.NewsModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends Adapter<NewsAdapter.ItemsHolder> {


    private List<NewsModel> list;
    private ItemListener clickLister;

    public void setClickLister(ItemListener clickLister) {
        this.clickLister = clickLister;
    }

    public NewsAdapter(List<NewsModel> list) {
        this.list = list;

    }

    @Override
    public ItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new, parent, false);
        return new ItemsHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemsHolder holder, int position) {
        final NewsModel item = list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvDate.setText(item.getDate());
        holder.tvDescription.setText(item.getDescription());
        holder.tvTitle.setText(item.getTitle());
        if (!TextUtils.isEmpty(item.getLinkImage()))
            Glide.with(holder.itemView.getContext())
                    .load(item.getLinkImage())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.imgView);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemsHolder extends ViewHolder {
        @BindView(R.id.itemNew_imgView)
        RoundedImageView imgView;
        @BindView(R.id.itemNew_tvTitle)
        TextView tvTitle;
        @BindView(R.id.itemNew_tvDate)
        TextView tvDate;
        @BindView(R.id.itemNew_tvDiscription)
        TextView tvDescription;

        ItemsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (clickLister != null) {
                    clickLister.onItemClicked(getPosition());
                }
            });
        }

    }
}
