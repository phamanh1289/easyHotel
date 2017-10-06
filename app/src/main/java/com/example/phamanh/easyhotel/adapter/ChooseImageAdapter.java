package com.example.phamanh.easyhotel.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChooseImageAdapter extends RecyclerView.Adapter<ChooseImageAdapter.ChooseIamgeHolder> {

    private List<Bitmap> mData;
    private ItemListener mItemListener;

    public ChooseImageAdapter(List<Bitmap> data) {
        mData = data;
    }

    public void setItemListener(ItemListener itemListener) {
        mItemListener = itemListener;
    }

    @Override
    public ChooseIamgeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chooser_image, parent, false);
        return new ChooseIamgeHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseIamgeHolder holder, int position) {
        holder.ivMain.setImageBitmap(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class ChooseIamgeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemChooser_ivMain)
        ImageView ivMain;

        ChooseIamgeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> mItemListener.onItemClicked(getAdapterPosition()));
        }
    }
}
