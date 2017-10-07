package com.example.phamanh.easyhotel.admin.service;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
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
    private ActionServiceDialog dialog, dialogAdd;
    private ServiceDetailModel mServiceDetailModel = new ServiceDetailModel();
    private List<String> mDataService = new ArrayList<>();
    private List<String> mDataSearch = new ArrayList<>();
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
        adapter.setItemListener(toClick);
        rvMain.setAdapter(adapter);
        etSearch.addTextChangedListener(loadChangeText);
        rvMain.setLayoutManager(new GridLayoutManager(getContext(), 2));
        if (mDataService.size() != 0)
            mDataService.clear();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    ItemListener toClick = new ItemListener() {
        @Override
        public void onItemClicked(int pos) {
            if (dialog == null)
                dialog = new ActionServiceDialog(getActivity(), "Do you want process item service ?", "Delete", "Update");
            dialog.show();
            dialog.setOnItemClickListener(new DialogListener() {
                @Override
                public void onConfirmClicked() {
                    if (!dialog.getEditText().trim().isEmpty()) {
                        mDataService.remove(pos);
                        mDataService.add(pos, dialog.getEditText());
                        adapter.notifyItemChanged(pos);
                        mServiceDetailModel.setService(mDataService);
                        refHotel_service.child("data").setValue(new Gson().toJson(mServiceDetailModel));
                    }
                }

                @Override
                public void onCancelClicked() {
                    mDataService.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    mServiceDetailModel.setService(mDataService);
                    refHotel_service.child("data").setValue(new Gson().toJson(mServiceDetailModel));
                }
            });
        }
    };

    @OnClick({R.id.baseSearch_ivSearch, R.id.fragAllService_tvAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragAllService_tvAdd:
                if (dialogAdd == null)
                    dialogAdd = new ActionServiceDialog(getContext(), "Please input new service", "Cancel", "Submit");
                dialogAdd.show();
                dialogAdd.setOnItemClickListener(new DialogListener() {
                    @Override
                    public void onConfirmClicked() {
                        if (!dialogAdd.getEditText().trim().isEmpty()) {
                            mDataService.add(dialogAdd.getEditText().trim());
                            mServiceDetailModel.setService(mDataService);
                            refHotel_service.child("data").setValue(new Gson().toJson(mServiceDetailModel));
                        }
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
                break;
            case R.id.baseSearch_ivSearch:
                etSearch.setText("");
                break;
        }
    }

    TextWatcher loadChangeText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etSearch.getText().length() == 0) {
                ivSearch.setVisibility(View.GONE);
                adapter = new AllServiceAdapter(mDataService);
            } else {
                ivSearch.setVisibility(View.VISIBLE);
                toSearchHotel(etSearch.getText().toString(), mDataService);
                adapter = new AllServiceAdapter(mDataSearch);
            }
            rvMain.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setItemListener(toClick);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void toSearchHotel(String name, List<String> mDataInfo) {
        if (mDataSearch.size() != 0)
            mDataSearch.clear();
        for (String item : mDataInfo) {
            if (item.toLowerCase().matches(".*" + name + ".*")) {
                mDataSearch.add(item);
            }
        }
    }
}
