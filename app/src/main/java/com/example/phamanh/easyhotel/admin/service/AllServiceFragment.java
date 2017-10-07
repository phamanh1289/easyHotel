package com.example.phamanh.easyhotel.admin.service;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appscyclone.aclibrary.view.ACRecyclerView;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
import com.example.phamanh.easyhotel.other.view.ActionServiceDialog;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AllServiceFragment extends BaseFragment {


    @BindView(R.id.actionbar_imgBack)
    FrameLayout imgBack;
    @BindView(R.id.baseSearch_etSearch)
    EditText etSearch;
    @BindView(R.id.baseSearch_ivSearch)
    ImageView ivSearch;
    @BindView(R.id.fragAllService_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;
    private ActionServiceDialog dialog;
    private ServiceDetailModel mServiceDetailModel = new ServiceDetailModel();
    private List<String> mDataService = new ArrayList<>();
    private AllServiceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_service, container, false);
        setActionBar(view, "All service");
        KeyboardUtils.setupUI(view, getActivity());
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        adapter = new AllServiceAdapter(mDataService);
        adapter.setItemListener(toDialogClick);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new GridLayoutManager(getContext(),2));
        dialog = new ActionServiceDialog(getActivity());
        toGetDataService();
    }

    private void toGetDataService() {
        showLoading();
        refHotel_service.child("data").addListenerForSingleValueEvent(toAddService);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                dismissLoading();
            }
        }.start();
    }

    ValueEventListener toAddService = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.getKey().equals("data")) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        mServiceDetailModel = gson.fromJson(jsonObject.toString(), ServiceDetailModel.class);
                        mDataService.addAll(mServiceDetailModel.getService());
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    DialogListener toDialogClick = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            if (dialog.getEditText().isEmpty()) {

            }
        }

        @Override
        public void onCancelClicked() {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    ACRecyclerView.OnItemListener toClick = (view, position) -> {
        toChangFragment(position);
    };

    private void toChangFragment(int pos) {
        dialog.setOnItemClickListener(toDialogClick);
    }

    @OnClick(R.id.baseSearch_ivSearch)
    public void onViewClicked() {
        etSearch.setText("");
    }
}
