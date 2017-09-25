package com.example.phamanh.easyhotel.adapter;


import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<CommentModel> mData;

    public CommentAdapter(List<CommentModel> data) {
        mData = data;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        CommentModel item = mData.get(position);
        Glide.with(holder.itemView.getContext()).load(item.getImage()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true).override(150, 150).centerCrop()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.avLoading.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.ivUser);
        holder.tvDescription.setText(item.getContent());
        holder.tvUser.setText(item.getEmail());
        holder.tvDate.setText(AppUtils.getTimeAgo(item.getTime()));
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemComment_ivUser)
        ImageView ivUser;
        @BindView(R.id.itemComment_tvDescription)
        TextView tvDescription;
        @BindView(R.id.itemComment_tvUserName)
        TextView tvUser;
        @BindView(R.id.itemComment_tvDate)
        TextView tvDate;
        @BindView(R.id.item_avLoading)
        AVLoadingIndicatorView avLoading;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
