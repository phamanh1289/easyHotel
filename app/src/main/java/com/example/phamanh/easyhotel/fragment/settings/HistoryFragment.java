package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.HistoryAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.BookingModel;
import com.example.phamanh.easyhotel.model.HistoryModel;
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


public class HistoryFragment extends BaseFragment {

    @BindView(R.id.fragHistory_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;
    private List<HistoryModel> mData = new ArrayList<>();
    private HistoryAdapter adapter;
    private HistoryModel mHistoryModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setActionBar(view, getString(R.string.history));
        setVisibilityTabBottom(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        showLoading();
        adapter = new HistoryAdapter(mData);
        adapter.setItemListener(toClick);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mData.size() != 0)
            mData.clear();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                dismissLoading();
            }
        }.start();
        refMember_history.child(mUser.getUid()).addChildEventListener(toAddHistory);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            refMember_history.removeEventListener(toAddHistory);
        } catch (Exception e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            refMember_history.removeEventListener(toAddHistory);
        } catch (Exception e) {
        }
    }

    ChildEventListener toAddHistory = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            showLoading();
            if (dataSnapshot.getValue() != null) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        mHistoryModel = gson.fromJson(jsonObject.toString(), HistoryModel.class);
                        mData.add(0, mHistoryModel);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
    };

    ItemListener toClick = new ItemListener() {
        @Override
        public void onItemClicked(int pos) {

            if (mData.get(pos).getDiscription() instanceof BookingModel) {
                replaceFragment(NotificationDetailFragment.newInstance(mData.get(pos).getDiscription()), true);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
