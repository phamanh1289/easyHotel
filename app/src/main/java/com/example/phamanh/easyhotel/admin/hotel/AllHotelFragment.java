package com.example.phamanh.easyhotel.admin.hotel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.appscyclone.aclibrary.view.ACRecyclerView;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.Location;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AllHotelFragment extends BaseFragment {

    @BindView(R.id.fragAllHotel_rvMain)
    ACRecyclerView rvMain;
    @BindView(R.id.actionbar_imgBack)
    FrameLayout imgBack;
    @BindView(R.id.baseSearch_etSearch)
    EditText etSearch;
    @BindView(R.id.baseSearch_ivSearch)
    ImageView ivSearch;

    Unbinder unbinder;

    private List<InfomationModel> mDataInfo = new ArrayList<>();
    private List<InfomationModel> mDataSearch = new ArrayList<>();
    private InfomationModel info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_hotel, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "All Hotel");
        KeyboardUtils.setupUI(view, getActivity());
        imgBack.setVisibility(View.GONE);
        init();
        return view;
    }

    private void init() {
        rvMain.setAdapter(AllHotelAdapter.class, mDataInfo);
        rvMain.setOnItemListener(toClick, new Integer[]{R.id.itemViewHotel_clLayout}, true);
        etSearch.addTextChangedListener(loadChangeText);
        showLoading();
        toGetDataProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    ACRecyclerView.OnItemListener toClick = (view, position) -> Toast.makeText(getActivity(), mDataInfo.get(position).getName(), Toast.LENGTH_SHORT).show();


    private void toGetDataProfile() {
        if (mDataInfo.size() != 0)
            mDataInfo.clear();
        refHotel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                info = new InfomationModel();
                info.setId(dataSnapshot.getKey());
                info.setPrice(dataSnapshot.child("price").getValue().toString());
                info.setName(dataSnapshot.child("name").getValue().toString());
                info.setLogo(dataSnapshot.child("logo").getValue().toString());
                info.setDescription(dataSnapshot.child("description").getValue().toString());
                info.setAddress(dataSnapshot.child("address").getValue().toString());
                info.setLocation(new Location(dataSnapshot.child("location").child("lat").getValue().toString(), dataSnapshot.child("location").child("lng").getValue().toString()));
                info.setDataImage((List<String>) dataSnapshot.child("mDataImage").getValue());
                mDataInfo.add(info);
                rvMain.notifyItemInserted(mDataInfo.size());
                dismissLoading();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    TextWatcher loadChangeText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etSearch.getText().length() == 0) {
                ivSearch.setVisibility(View.GONE);
                rvMain.setAdapter(AllHotelAdapter.class, mDataInfo);
                rvMain.notifyDataSetChanged();
            } else {
                ivSearch.setVisibility(View.VISIBLE);
                toSearchHotel(etSearch.getText().toString(), mDataInfo);
                rvMain.setAdapter(AllHotelAdapter.class, mDataSearch);
                rvMain.notifyDataSetChanged();
            }
            rvMain.setOnItemListener(toClick, new Integer[]{R.id.itemViewHotel_clLayout}, true);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void toSearchHotel(String name, List<InfomationModel> mDataInfo) {
        if (mDataSearch.size() != 0)
            mDataSearch.clear();
        for (InfomationModel item : mDataInfo) {
            if (item.getName().matches(".*" + name + ".*")) {
                mDataSearch.add(item);
            }
        }
    }

    @OnClick({R.id.fragAllHotel_tvAdd, R.id.baseSearch_ivSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragAllHotel_tvAdd:
                addFragment(new AddUserFragment(), true);
                break;
            case R.id.baseSearch_ivSearch:
                etSearch.setText("");
                break;
        }
    }
}
