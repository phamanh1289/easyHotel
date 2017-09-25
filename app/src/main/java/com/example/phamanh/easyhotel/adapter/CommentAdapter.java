package com.example.phamanh.easyhotel.adapter;


import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<CommentModel> mData;
    private CommentModel item;

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
        item = mData.get(position);
        if (item.getImage().equals(""))
            holder.ivUser.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(item.getImage(), Base64.DEFAULT), 0, Base64.decode(item.getImage(), Base64.DEFAULT).length));
        else
            Glide.with(holder.itemView.getContext()).load(R.drawable.ic_no_image).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(holder.ivUser);
        holder.tvDescription.setText(item.getContent());
        holder.tvUser.setText(item.getEmail());
        holder.tvDate.setText(AppUtils.convertTime(item.getTime()));
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

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
