package com.example.phamanh.easyhotel.admin.user;

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
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AllUserFragment extends BaseFragment implements ACRecyclerView.OnItemListener {

    @BindView(R.id.fragAllUser_rvMain)
    ACRecyclerView rvMain;
    Unbinder unbinder;
    @BindView(R.id.actionbar_imgBack)
    FrameLayout imgBack;
    @BindView(R.id.baseSearch_etSearch)
    EditText etSearch;
    @BindView(R.id.baseSearch_ivSearch)
    ImageView ivSearch;

    private List<UserModel> mDataInfo = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "All User");
        KeyboardUtils.setupUI(view, getActivity());
        imgBack.setVisibility(View.GONE);
        init();
        return view;
    }

    private void init() {
        rvMain.setAdapter(AllUserAdapter.class, mDataInfo);
        rvMain.setOnItemListener(this, new Integer[]{R.id.itemViewUser_clLayout}, true);
        etSearch.addTextChangedListener(loadChangeText);
        showLoading();
        toGetDataProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClicked(View view, int position) {
        Toast.makeText(getActivity(), mDataInfo.get(position).getEmail(), Toast.LENGTH_SHORT).show();
    }

    private void toGetDataProfile() {
        if (mDataInfo.size() != 0)
            mDataInfo.clear();
        refMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);
                    mDataInfo.add(userModel);
                    rvMain.notifyDataSetChanged();
                    dismissLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            } else {
                ivSearch.setVisibility(View.VISIBLE);
                toSearchHotel(etSearch.getText().toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    private void toSearchHotel(String name) {

    }
}
