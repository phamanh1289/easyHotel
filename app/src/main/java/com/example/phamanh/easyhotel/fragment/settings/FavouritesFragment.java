package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.FavouritesAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.fragment.home.BookingCommentParrent;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.LikeMemberModel;
import com.example.phamanh.easyhotel.model.Location;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FavouritesFragment extends BaseFragment {


    @BindView(R.id.fragFavourites_rvMain)
    RecyclerView rvMain;
    @BindView(R.id.fragFavourites_tvNoData)
    TextView tvNoData;
    Unbinder unbinder;

    private List<InfomationModel> mDataInfomation = new ArrayList<>();
    private InfomationModel info;
    private FavouritesAdapter adapter;
    private List<LikeMemberModel> mDataLike = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        setActionBar(view, getString(R.string.favourites));
        setVisibilityTabBottom(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            refMember_like.removeEventListener(toAddHotel);
        } catch (Exception e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            refMember_like.removeEventListener(toAddHotel);
        } catch (Exception e) {
        }
    }

    private void init() {
        adapter = new FavouritesAdapter(mDataInfomation);
        adapter.setItemListener(toClickModel);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        toGetListHotel();
    }

    ItemListener toClickModel = new ItemListener() {
        @Override
        public void onItemClicked(int pos) {
            addFragment(BookingCommentParrent.newInstance(mDataInfomation.get(pos), pos), true);
        }
    };

    private void toGetListHotel() {
        showLoading();
        if (mDataLike.size() != 0)
            mDataLike.clear();
        refMember_like.child(mUser.getUid()).addChildEventListener(toAddHotel);
        dismissLoading();
    }

    ChildEventListener toAddHotel = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() != null) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        LikeMemberModel item = gson.fromJson(jsonObject.toString(), LikeMemberModel.class);
                        mDataLike.add(item);
                        toGetDataHotel(item.getHotel());
                        if (mDataLike.size() != 0) {
                            tvNoData.setVisibility(View.GONE);
                        } else tvNoData.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ignored) {
                    dismissLoading();
                }
            } else {
                dismissLoading();
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        LikeMemberModel item = gson.fromJson(jsonObject.toString(), LikeMemberModel.class);
                        mDataLike.remove(item);
                        for (int i = 0; i < mDataInfomation.size(); i++) {
                            if (item.getHotel().equals(mDataInfomation.get(i).getId())) {
                                mDataInfomation.remove(i);
                                adapter.notifyItemRemoved(i);
                                tvNoData.setVisibility(mDataInfomation.size() == 0 ? View.VISIBLE : View.GONE);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception ignored) {
                    dismissLoading();
                }
            } else {
                dismissLoading();
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void toLoadData(List<LikeMemberModel> mData) {
        for (LikeMemberModel item : mData) {
            toGetDataHotel(item.getHotel());
        }
        dismissLoading();
    }

    private void toGetDataHotel(String key) {
        refHotel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(key)) {
                    info = new InfomationModel();
                    info.setId(dataSnapshot.getKey());
                    info.setPrice(dataSnapshot.child("price").getValue().toString());
                    info.setName(dataSnapshot.child("name").getValue().toString());
                    info.setLogo(dataSnapshot.child("logo").getValue().toString());
                    info.setDescription(dataSnapshot.child("description").getValue().toString());
                    info.setAddress(dataSnapshot.child("address").getValue().toString());
                    info.setLocation(new Location(dataSnapshot.child("location").child("lat").getValue().toString(), dataSnapshot.child("location").child("lng").getValue().toString()));
                    info.setDataImage((List<String>) dataSnapshot.child("mDataImage").getValue());
                    mDataInfomation.add(info);
                    adapter.notifyItemInserted(mDataInfomation.size());
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
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
