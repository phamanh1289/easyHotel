package com.example.phamanh.easyhotel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.PlaceModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nhat Hoang on 20/03/2017.
 */

public class PlaceNavDrawerAdapter extends RecyclerView.Adapter<PlaceNavDrawerAdapter.PlaceNavDrawerHolder>{

    private ItemListener clickLister;
    private List<PlaceModel> placeModelList;

    public void setClickLister(ItemListener clickLister){
        this.clickLister = clickLister;
    }

    public PlaceNavDrawerAdapter(List<PlaceModel> placeModelList) {
        this.placeModelList = placeModelList;
    }

    @Override
    public PlaceNavDrawerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_position,parent,false);
        return new PlaceNavDrawerHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceNavDrawerHolder holder, final int position) {
        final PlaceModel placeModel = placeModelList.get(position);
        holder.itemListPosition_tvTitle.setText(placeModel.getmTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), placeModel.getmTitle(), Toast.LENGTH_SHORT).show();
                if(clickLister != null)
                    clickLister.onItemClicked(position);
            }
        });
        //holder.itemListPosition_tvPosition.setText(placeModel.getLatLng().toString());
    }

    @Override
    public int getItemCount() {
        return placeModelList != null ? placeModelList.size() : 0;
    }

    class PlaceNavDrawerHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.itemListPosition_tvTitle)
        TextView itemListPosition_tvTitle;
//        @BindView(R.id.itemListPosition_tvPosition)
//        TextView itemListPosition_tvPosition;

        PlaceNavDrawerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
