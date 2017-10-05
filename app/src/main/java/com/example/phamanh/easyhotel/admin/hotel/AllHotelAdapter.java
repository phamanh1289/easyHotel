package com.example.phamanh.easyhotel.admin.hotel;


import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appscyclone.aclibrary.view.ACRecyclerView;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllHotelAdapter extends ACRecyclerView.ACBaseViewHolder<InfomationModel> {

    @BindView(R.id.itemNewViewHotel_imgView)
    RoundedImageView imgView;
    @BindView(R.id.itemNewViewHotel_tvTitle)
    TextView tvTitle;
    @BindView(R.id.itemNewViewHotel_tvDiscription)
    TextView tvDiscription;
    @BindView(R.id.item_avLoading)
    AVLoadingIndicatorView avLoading;
    public StorageReference refStorage;

    public AllHotelAdapter(ViewGroup parent, @LayoutRes int res) {
        super(parent, R.layout.item_view_hotel);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(InfomationModel data) {
        tvTitle.setText(data.getName());
        tvDiscription.setText(data.getDescription());
        refStorage = FirebaseStorage.getInstance().getReferenceFromUrl(data.getLogo());
        refStorage.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(bytes -> {
            imgView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            avLoading.setVisibility(View.GONE);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                imgView.setImageResource(R.drawable.ic_no_image);
                avLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreatedView(View view) {

    }
}
