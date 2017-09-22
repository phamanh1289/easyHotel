package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.HotelMainAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.HotelModel;
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


public class HomeFragment extends BaseFragment {

    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;
    @BindView(R.id.fragHome_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;

    private List<HotelModel> mDataHotel = new ArrayList<>();
    private HotelMainAdapter adapter;
    private HotelModel item = new HotelModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        KeyboardUtils.hideSoftKeyboard(getActivity());
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home));
        unbinder = ButterKnife.bind(this, view);
        ivBack.setVisibility(View.GONE);
        init();
        return view;
    }

    private void init() {
        adapter = new HotelMainAdapter(mDataHotel);
        adapter.setItemListener(toClickItem);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        toAddData();
    }

    private void toAddData() {
        if (mDataHotel.size() != 0)
            mDataHotel.clear();
        refHotel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                item.setId(dataSnapshot.getKey());
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    item = gson.fromJson(jsonObject.toString(), HotelModel.class);
                    mDataHotel.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
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

    ItemListener toClickItem = pos -> addFragment(HomeDetailFragment.newInstance(mDataHotel.get(pos)), true);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
