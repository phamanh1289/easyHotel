package com.example.phamanh.easyhotel.other.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.RatingAdapter;
import com.example.phamanh.easyhotel.model.RatingModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class RatingDialog extends Dialog {
    @BindView(R.id.viewRating_rvRating)
    RecyclerView rvRating;

    private List<RatingModel> mDataRaring = new ArrayList<>();
    private RatingAdapter adapter;
    Unbinder unbinder;

    public void setDataRaring(List<RatingModel> dataRaring) {
        mDataRaring = dataRaring;
    }

    public RatingDialog(@NonNull Context context, List<RatingModel> mDataRaring) {
        super(context, android.R.style.Theme_Holo_Dialog);
        this.mDataRaring = mDataRaring;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setDimAmount(0.3f);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setContentView(R.layout.view_dialog_choose_image);

        ButterKnife.bind(this);
        adapter = new RatingAdapter(mDataRaring);
        rvRating.setAdapter(adapter);
        rvRating.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @OnClick({R.id.viewRating_tvCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewRating_tvCancel:
                dismiss();
                break;
        }
    }

}
