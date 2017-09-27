package com.example.phamanh.easyhotel.adapter;


import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<CommentModel> mData;
    public StorageReference refStorage;

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
        refStorage = FirebaseStorage.getInstance().getReferenceFromUrl(item.getImage());
        refStorage.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(bytes -> {
            holder.ivUser.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            holder.avLoading.setVisibility(View.GONE);
        }).addOnFailureListener(exception -> {
            holder.ivUser.setImageResource(R.drawable.ic_no_image);
            holder.avLoading.setVisibility(View.GONE);
        });
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
