package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.RatingModel;
import com.example.phamanh.easyhotel.other.view.RatingDialog;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.RotationRatingBar;

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
    ImageView ivBanner;
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

    private InfomationModel mInfomationModel;
    private int rating;
    public List<RatingModel> mDataRating = new ArrayList<>();
    private String mKey;
    private RatingModel mRatingModel;
    private RatingDialog ratingDialog;
    private boolean isCheckNoData, isCheckRating;
    private View view;

    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_detail, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home_detail));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataRating.size() != 0)
            mDataRating.clear();
        unbinder = ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        showLoading();
        mInfomationModel = ((BookingCommentParrent) getParentFragment()).mInfomationModel;
        mKey = ((BookingCommentParrent) getParentFragment()).mKey;
        avLoading.setVisibility(tvScore.getText().toString().length() != 0 ? View.GONE : View.VISIBLE);
        if (mInfomationModel != null) {
            tvTitle.setText(mInfomationModel.getName());
            tvDescription.setText(mInfomationModel.getDescription());
            ivBanner.setImageBitmap(mInfomationModel.getBitmap());
        }
        ratingStar.setEmptyDrawableRes(R.drawable.ic_no_start);
        ratingStar.setFilledDrawableRes(R.drawable.ic_start);
        refHotel_rating.child(mKey).addChildEventListener(toAddRating);

        if (!isCheckNoData) {
            dismissLoading();
            avLoading.setVisibility(View.GONE);
            tvScore.setText("-");
            isCheckRating = true;
            toPostRating();
        }
    }

    private void toPostRating() {
        if (isCheckRating) {
            ratingStar.setOnRatingChangeListener((baseRatingBar, v) ->
            {
                rating = (int) v;
                AppUtils.showAlertConfirm(getContext(), "Would you like to rating  for the hotel ?", toRating);
            });
        }
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
                    ratingStar.setRating(Float.parseFloat(mRatingModel.getScore()));
                    isCheckRating = true;
                    toPostRating();
                    avLoading.setVisibility(mDataRating.size() != 0 ? View.GONE : View.VISIBLE);
                    isCheckNoData = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dismissLoading();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.getValue() != null) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        mRatingModel = gson.fromJson(jsonObject.toString(), RatingModel.class);
                        mDataRating.add(mRatingModel);
                    }
                    tvScore.setText(toCountScore(mDataRating));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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

    @OnClick(R.id.fragHomeDetail_tvScore)
    public void onViewClicked() {
        if (avLoading.getVisibility() != View.VISIBLE) {
            ratingDialog = new RatingDialog(getContext(), mDataRating);
            ratingDialog.show();
        }
    }
}
