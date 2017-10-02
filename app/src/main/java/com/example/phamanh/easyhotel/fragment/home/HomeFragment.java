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
import com.example.phamanh.easyhotel.fragment.settings.ProfileFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.model.HistoryModel;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.ListComment;
import com.example.phamanh.easyhotel.model.ListRating;
import com.example.phamanh.easyhotel.model.Location;
import com.example.phamanh.easyhotel.model.RatingModel;
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

    private List<InfomationModel> mDataInfomation = new ArrayList<>();
    private HotelMainAdapter adapter;
    private HotelModel hotel = new HotelModel();
    private List<CommentModel> mDataComment = new ArrayList<>();
    private ListComment mListComment = new ListComment();
    private List<RatingModel> mDataRating = new ArrayList<>();
    private ListRating mListRating = new ListRating();
    private ServiceDetailModel service = new ServiceDetailModel();
    private InfomationModel info = new InfomationModel();
    private List<String> mDataImage = new ArrayList<>();

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
        adapter = new HotelMainAdapter(mDataInfomation);
        adapter.setItemListener(toClickItem);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        showLoading();
        if (getUser() == null) {
            toGetDataProfile();
        }
        toGetDataHotel();
//        toAddDataDemo();
    }

    private void toGetDataProfile() {
        refMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (mUser.getUid().equals(dataSnapshot.getKey())) {
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

    DialogListener toUpdateProfile = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            addFragment(new ProfileFragment(), true);
        }

        @Override
        public void onCancelClicked() {

        }
    };

    ItemListener toClickItem = pos -> addFragment(BookingCommentParrent.newInstance(mDataInfomation.get(pos), pos), true);

    private void toGetDataHotel() {
        if (mDataInfomation.size() != 0)
            mDataInfomation.clear();
        refHotel.addChildEventListener(toGetHotel);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            refHotel.removeEventListener(toGetHotel);
        } catch (Exception e) {
        }
    }

    ChildEventListener toGetHotel = new ChildEventListener() {
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
            mDataInfomation.add(info);
            adapter.notifyItemInserted(mDataInfomation.size());
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void toAddDataDemo() {

        refMember_history.child(mUser.getUid()).push().setValue(new Gson().toJson(new HistoryModel("1")));
        refMember_history.child(mUser.getUid()).push().setValue(new Gson().toJson(new HistoryModel("2")));
        refMember_history.child(mUser.getUid()).push().setValue(new Gson().toJson(new HistoryModel("3")));

//        String mKeyHotel = String.valueOf(System.currentTimeMillis());

//        mDataComment.add(new CommentModel(System.currentTimeMillis(), "email_1", "nice", ""));
//        mDataComment.add(new CommentModel(System.currentTimeMillis(), "email_2", "bad", ""));
//        mListComment.comment = mDataComment;
//
//        mDataRating.add(new RatingModel("email_1", "1", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_2", "2", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_3", "3", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_4", "4", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_5", "5", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_6", "6", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_7", "7", System.currentTimeMillis()));
//        mDataRating.add(new RatingModel("email_8", "8", System.currentTimeMillis()));
//        mListRating.rating = mDataRating;

//        List<String> mdata = new ArrayList<>();
//        mdata.add("spa");
//        mdata.add("eat");
//        service.setService(mdata);
//        info.setAddress("123/123");
//        info.setDescription("demo add");
//        info.setLogo("demo logo");
//        info.setName("De Nhat Demo");
//        info.setPrice("1200");
//        info.setLocation(new Location("10.786968", "106.666520"));
//        mDataImage.add("image_1");
//        mDataImage.add("image_2");
//        info.setDataImage(mDataImage);
//        hotel.setRoom(new RoomModel(12, 8));


//        hotel.setDataComment(mDataComment);
//        hotel.setDataRating(mDataRating);
//        hotel.service = mDataServiceDetail;

//        hotel.setInfomation(info);
//        refHotel.child(String.valueOf(System.currentTimeMillis())).setValue(new Gson().toJson(hotel));

//        refHotel_comment.child(mKey).setValue(new Gson().toJson(mListComment));
//        refHotel_service.child("1506252747351").setValue(new Gson().toJson(service));
//        refHotel_service.child("1506252754506").setValue(new Gson().toJson(service));
//        refHotel_service.child(mKey).setValue(new Gson().toJson(mListService));
//        refHotel.child(mKey).setValue(new Gson().toJson(info));
//        refHotel_room.child(mKey).setValue(new Gson().toJson(new RoomModel(12, 8)));
    }
}
