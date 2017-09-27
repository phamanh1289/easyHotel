package com.example.phamanh.easyhotel.adapter;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HotelMainAdapter extends RecyclerView.Adapter<HotelMainAdapter.HotelHolder> {

    private List<InfomationModel> mData;
    private ItemListener itemListener;
    private InfomationModel model;
    public StorageReference refStorage;
    private DatabaseReference database;

    public HotelMainAdapter(List<InfomationModel> mData) {
        this.mData = mData;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public HotelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HotelHolder(view);
    }

    @Override
    public void onBindViewHolder(HotelHolder holder, int position) {
        model = mData.get(position);
        holder.tvAddress.setText(model.getAddress());
        holder.tvTitle.setText(model.getName());
        refStorage = FirebaseStorage.getInstance().getReferenceFromUrl(model.getLogo());
        refStorage.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                holder.ivBanner.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                holder.avLoading.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                holder.ivBanner.setImageResource(R.drawable.ic_no_image);
                holder.avLoading.setVisibility(View.GONE);
            }
        });

        database = FirebaseDatabase.getInstance().getReference("hotel").child(Constant.ROOM);
        database.child(model.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.ivDouble.setImageResource(Integer.parseInt(dataSnapshot.child("double").getValue().toString()) != 0 ? R.drawable.ic_double_room_ok : R.drawable.ic_double_room_null);
                holder.ivSingle.setImageResource(Integer.parseInt(dataSnapshot.child("single").getValue().toString()) != 0 ? R.drawable.ic_signle_room_ok : R.drawable.ic_signle_room);
            holder.tvBooking.setText(Integer.parseInt(dataSnapshot.child("double").getValue().toString()) == 0 && Integer.parseInt(dataSnapshot.child("double").getValue().toString()) == 0 ? holder.itemView.getContext().getString(R.string.out_room) : holder.itemView.getContext().getString(R.string.booking));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() : 0;
    }

    class HotelHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemHome_tvTitle)
        TextView tvTitle;
        @BindView(R.id.itemHome_tvAddress)
        TextView tvAddress;
        @BindView(R.id.itemHome_ivBanner)
        ImageView ivBanner;
        @BindView(R.id.item_avLoading)
        AVLoadingIndicatorView avLoading;
        @BindView(R.id.itemHome_ivDouble)
        ImageView ivDouble;
        @BindView(R.id.itemHome_ivSingle)
        ImageView ivSingle;
        @BindView(R.id.itemHome_tvBooking)
        TextView tvBooking;

        public HotelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> itemListener.onItemClicked(getAdapterPosition()));
        }
    }
}
