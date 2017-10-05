package com.example.phamanh.easyhotel.admin.user;


import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appscyclone.aclibrary.view.ACRecyclerView;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllUserAdapter extends ACRecyclerView.ACBaseViewHolder<UserModel> {

    @BindView(R.id.itemNewViewUser_imgView)
    RoundedImageView ivView;
    @BindView(R.id.itemNewViewUser_tvTitle)
    TextView tvTitle;
    @BindView(R.id.itemNewViewUser_tvDiscription)
    TextView tvDiscription;
    @BindView(R.id.item_avLoading)
    AVLoadingIndicatorView avLoading;
    public StorageReference refStorage;

    public AllUserAdapter(ViewGroup parent, @LayoutRes int res) {
        super(parent, R.layout.item_view_user);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(UserModel data) {
        tvTitle.setText(data.getFullName());
        tvDiscription.setText(data.getEmail());
        refStorage = FirebaseStorage.getInstance().getReferenceFromUrl(data.getAvatar());
        refStorage.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(bytes -> {
            ivView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            avLoading.setVisibility(View.GONE);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ivView.setImageResource(R.drawable.ic_no_image);
                avLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreatedView(View view) {

    }
}
