package com.example.phamanh.easyhotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.phamanh.easyhotel.model.PlaceAutocomplete;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by DangQuang on 8/16/17.
 **/

public class PlaceAdapter extends ArrayAdapter<PlaceAutocomplete> implements Filterable {

    private GoogleApiClient mGoogleApiClient;
    private ArrayList<PlaceAutocomplete> mResultList;

    public PlaceAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        return mResultList != null ? mResultList.size() : 0;
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mResultList = AppUtils.getPredictions(mGoogleApiClient, constraint);
                    if (mResultList != null) {
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}
