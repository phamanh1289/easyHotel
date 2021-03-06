package com.example.phamanh.easyhotel.fragment.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.ImageSearchPager;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.fragment.nearby.NearbyFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.EventBusBooking;
import com.example.phamanh.easyhotel.model.EventBusLocationModel;
import com.example.phamanh.easyhotel.model.HistoryModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.LikeMemberModel;
import com.example.phamanh.easyhotel.model.Location;
import com.example.phamanh.easyhotel.model.RatingModel;
import com.example.phamanh.easyhotel.other.view.RatingDialog;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.RotationRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class HomeDetailFragment extends BaseFragment {


    @BindView(R.id.fragHomeDetail_ivBanner)
    ViewPager viewpager;
    @BindView(R.id.fragHomeDetail_tvTitle)
    TextView tvTitle;
    @BindView(R.id.fragHomeDetail_tvDescription)
    TextView tvDescription;
    @BindView(R.id.fragHomeDetail_ratingStart)
    RotationRatingBar ratingStar;
    @BindView(R.id.fragHomeDetail_tvScore)
    EditText tvScore;
    @BindView(R.id.item_avLoading)
    AVLoadingIndicatorView avLoading;
    @BindView(R.id.fragHomeDetail_tvLike)
    EditText tvLike;
    @BindView(R.id.fragHomeDetail_tvCheck)
    EditText tvCheck;
    @BindView(R.id.fragHomeDetail_tvComment)
    EditText tvComment;
    @BindView(R.id.fragHomeDetail_ivLike)
    ImageView ivLike;
    @BindView(R.id.fragHomeDetail_tvSubmitRating)
    TextView tvSubmitRating;
    @BindView(R.id.fragHomeDetail_tvAddress)
    TextView tvAddress;
    @BindView(R.id.fragHomeDetail_tvGPS)
    TextView tvGPS;

    private InfomationModel mInfomationModel;
    private int rating, countStart, countLike;
    public List<RatingModel> mDataRating = new ArrayList<>();
    private List<Bitmap> mDataImage = new ArrayList<>();
    private ImageSearchPager adapterImage;
    private String mKey;
    private RatingModel mRatingModel;
    private RatingDialog ratingDialog;
    private boolean isCheckNoData;
    private Location mLocation;
    private View view;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_detail, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home_detail));
        unbinder = ButterKnife.bind(this, view);
        getBundle();
        init();
        return view;
    }

    private void getBundle() {
        mInfomationModel = ((BookingCommentParrent) getParentFragment()).mInfomationModel;
        mKey = ((BookingCommentParrent) getParentFragment()).mKey;
        ivLike.setImageResource(mInfomationModel.isLike ? R.drawable.ic_like_main : R.drawable.ic_no_lick_main);
    }

    private void init() {
        showLoading();
        if (mDataRating.size() != 0)
            mDataRating.clear();
        avLoading.setVisibility(tvScore.getText().toString().length() != 0 ? View.GONE : View.VISIBLE);
        if (mInfomationModel != null) {
            tvTitle.setText(mInfomationModel.getName());
            tvAddress.setText(mInfomationModel.getAddress());
            tvDescription.setText(mInfomationModel.getDescription());
            mLocation = mInfomationModel.getLocation();
        }
        ratingStar.setEmptyDrawableRes(R.drawable.ic_no_start);
        ratingStar.setFilledDrawableRes(R.drawable.ic_start);
        refHotel_rating.child(mKey).addChildEventListener(toAddRating);
        if (!isCheckNoData) {
            avLoading.setVisibility(View.GONE);
            tvScore.setText("-");
            tvCheck.setText("0");
            countStart = 0;
            ratingStar.setRating(countStart);
            dismissLoading();
        }
        ratingStar.setOnRatingChangeListener((baseRatingBar, v) ->
        {
            rating = (int) v;
            tvSubmitRating.setVisibility(countStart != rating ? View.VISIBLE : View.GONE);
        });
        refHotel_like.child(mKey).addChildEventListener(toAddLike);
        adapterImage = new ImageSearchPager(getContext(), mDataImage);
        viewpager.setAdapter(adapterImage);
        toLoadImageArray(mInfomationModel.getDataImage());
    }

    ChildEventListener toAddRating = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() != null) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        mRatingModel = gson.fromJson(jsonObject.toString(), RatingModel.class);
                        mDataRating.add(mRatingModel);
                    }
                    tvScore.setText(toCountScore(mDataRating));
                    tvCheck.setText(String.valueOf(mDataRating.size()));
                    EventBus.getDefault().postSticky(new EventBusBooking(Constant.HOME));
                    avLoading.setVisibility(mDataRating.size() != 0 ? View.GONE : View.VISIBLE);
                    isCheckNoData = true;
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
    };

    ChildEventListener toAddLike = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() != null) {
                tvLike.setText(String.valueOf(++countLike));
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
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            refHotel_rating.removeEventListener(toAddRating);
        } catch (Exception e) {

        }
    }

    DialogListener toRating = new DialogListener() {
        @Override
        public void onConfirmClicked() {

            mRatingModel = new RatingModel(getUser().getEmail(), String.valueOf(rating), System.currentTimeMillis());
            refHotel_rating.child(mKey).child(mUser.getUid()).setValue(new Gson().toJson(mRatingModel));
            for (int i = 0; i < mDataRating.size(); i++) {
                if (mRatingModel.getEmail().equals(mDataRating.get(i).getEmail())) {
                    mDataRating.remove(i);
                    mDataRating.add(i, mRatingModel);
                }
            }
            refMember_history.child(mUser.getUid()).push().setValue(new Gson().toJson(new HistoryModel(Constant.MESS_RATING + mInfomationModel.getName(), System.currentTimeMillis(), mInfomationModel.getId())));
            tvScore.setText(toCountScore(mDataRating));
            tvSubmitRating.setVisibility(View.GONE);
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

    private String toCountScore(List<RatingModel> mData) {
        int score = 0;
        if (mData.size() != 0) {
            for (RatingModel item : mData) {
                score += Integer.parseInt(item.getScore());
            }
            return String.format("%.01f", (float) score / mData.size());
        }
        return "0";
    }

    @OnClick({R.id.fragHomeDetail_tvScore, R.id.fragHomeDetail_ivLike, R.id.fragHomeDetail_tvSubmitRating, R.id.fragHomeDetail_tvGPS})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragHomeDetail_tvScore:
                if (avLoading.getVisibility() != View.VISIBLE) {
                    ratingDialog = new RatingDialog(getContext(), mDataRating);
                    ratingDialog.show();
                }
                break;
            case R.id.fragHomeDetail_ivLike:
                if (!mInfomationModel.isLike) {
                    refHotel_like.child(mKey).child(mUser.getUid()).setValue(mUser.getUid());
                    refMember_like.child(mUser.getUid()).child(mKey).setValue(new Gson().toJson(new LikeMemberModel(mKey)));
                    refMember_history.child(mUser.getUid()).push().setValue(new Gson().toJson(new HistoryModel(Constant.MESS_LIKE + mInfomationModel.getName(), System.currentTimeMillis(), mInfomationModel.getId())));
                } else {
                    refHotel_like.child(mKey).child(mUser.getUid()).removeValue();
                    refMember_like.child(mUser.getUid()).child(mKey).removeValue();
                    refMember_history.child(mUser.getUid()).push().setValue(new Gson().toJson(new HistoryModel(Constant.MESS_DISLIKE + mInfomationModel.getName(), System.currentTimeMillis(), mInfomationModel.getId())));
                }
                mInfomationModel.isLike = !mInfomationModel.isLike;
                ivLike.setImageResource(mInfomationModel.isLike ? R.drawable.ic_like_main : R.drawable.ic_no_lick_main);
                break;
            case R.id.fragHomeDetail_tvSubmitRating:
                AppUtils.showAlertConfirm(getContext(), "Would you like to rating  for the hotel ?", toRating);
                break;
            case R.id.fragHomeDetail_tvGPS:
                mLocation = mInfomationModel.getLocation();
                EventBus.getDefault().postSticky(new EventBusLocationModel(Double.parseDouble(mLocation.getLat()), Double.parseDouble(mLocation.getLng())));
                addFragment(new NearbyFragment(),true);
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusBooking event) {
        if (event.getAction().equals(Constant.COMMENT)) {
            tvComment.setText(event.getValue());
        } else if (event.getAction().equals(Constant.HOME)) {
            for (RatingModel item : mDataRating) {
                if (item.getEmail().equals(getUser().getEmail())) {
                    countStart = (int) Float.parseFloat(item.getScore());
                    ratingStar.setRating(countStart);
                }
            }
            dismissLoading();
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void toLoadImageArray(List<String> mData) {
        for (String s : mData) {
            baseStore = FirebaseStorage.getInstance().getReferenceFromUrl(s);
            baseStore.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(bytes -> {
                mDataImage.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                if (mDataImage.size() == mData.size()) {
                    mDataImage.add(0, mInfomationModel.getBitmap());
                    dismissLoading();
                }
                adapterImage.notifyDataSetChanged();
            }).addOnFailureListener(exception -> {
            });
        }
    }
}
