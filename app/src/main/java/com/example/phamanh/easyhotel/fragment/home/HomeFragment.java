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
import com.example.phamanh.easyhotel.base.BaseApplication;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.Location;
import com.example.phamanh.easyhotel.model.RatingModel;
import com.example.phamanh.easyhotel.model.RoomModel;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
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
        toGetProfile();
//        toAddDataDemo();
        toGetDataHotel();
    }

    private void toGetProfile() {
        showLoading();
        refMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(mUser.getUid())) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                        UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);
                        BaseApplication application = (BaseApplication) getActivity().getApplication();
                        application.setCustomer(userModel);
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
        });
    }

    private void toGetDataHotel() {
        if (mDataHotel.size() != 0)
            mDataHotel.clear();
        refHotel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                item.setId(dataSnapshot.getKey());
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null)
                        item = gson.fromJson(jsonObject.toString(), HotelModel.class);
                    mDataHotel.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
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

    ItemListener toClickItem = new ItemListener() {
        @Override
        public void onItemClicked(int pos) {
            addFragment(BookingCommentParrent.newInstance(mDataHotel.get(pos)), true);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void toAddDataDemo() {
        HotelModel hotel = new HotelModel();
        List<CommentModel> mDataComment = new ArrayList<>();
        List<RatingModel> mDataRating = new ArrayList<>();
        List<ServiceDetailModel> mDataServiceDetail = new ArrayList<>();
        InfomationModel info = new InfomationModel();
        List<String> mDataImage = new ArrayList<>();
        Location location = new Location("100", "200");

        mDataComment.add(new CommentModel(System.currentTimeMillis(), "email_1", "nice", ""));
        mDataComment.add(new CommentModel(System.currentTimeMillis(), "email_2", "bad", ""));

        mDataRating.add(new RatingModel("email_1", "1", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_2", "2", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_3", "3", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_4", "4", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_5", "5", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_6", "6", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_7", "7", System.currentTimeMillis()));
        mDataRating.add(new RatingModel("email_8", "8", System.currentTimeMillis()));

        mDataServiceDetail.add(new ServiceDetailModel("spa"));
        mDataServiceDetail.add(new ServiceDetailModel("eat"));

        info.setAddress("123/123");
        info.setDescription("demo add");
        info.setLogo("demo logo");
        info.setName("De Nhat Demo");
        info.setPrice("1200");
        info.setLocation(location);
        hotel.setRoom(new RoomModel(12, 8));


        hotel.setDataComment(mDataComment);
        hotel.setDataRating(mDataRating);
        hotel.service = mDataServiceDetail;
        mDataImage.add("image_1");
        mDataImage.add("image_2");
        info.setDataImage(mDataImage);
        hotel.setInfomation(info);
        refHotel.child(String.valueOf(System.currentTimeMillis())).setValue(new Gson().toJson(hotel));
    }
}
