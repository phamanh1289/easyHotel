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
import com.example.phamanh.easyhotel.model.EventBusBooking;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.ListRating;
import com.example.phamanh.easyhotel.model.RatingModel;
import com.example.phamanh.easyhotel.other.view.RatingDialog;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.RotationRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private RatingDialog ratingDialog;

    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home_detail));
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mInfomationModel = ((BookingCommentParrent) getParentFragment()).mInfomationModel;
        mKey = ((BookingCommentParrent) getParentFragment()).mKey;
        avLoading.setVisibility(tvScore.getText().toString().length() != 0 ? View.GONE : View.VISIBLE);
        if (mInfomationModel != null) {
            tvTitle.setText(mInfomationModel.getName());
            tvDescription.setText(mInfomationModel.getDescription());
            ivBanner.setImageBitmap(AppUtils.toChangeString(mInfomationModel.getLogo()));
        }
        ratingStar.setEmptyDrawableRes(R.drawable.ic_no_start);
        ratingStar.setFilledDrawableRes(R.drawable.ic_start);
        ratingStar.setOnRatingChangeListener((baseRatingBar, v) -> {
            rating = (int) v;
            AppUtils.showAlert(getContext(), getString(R.string.complete), "Would you like to rating " + rating + " for the hotel ?", toRating);
        });
    }

    DialogListener toRating = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            tvScore.setText(String.valueOf(rating));
            mDataRating.add(new RatingModel(getUser().getEmail(), tvScore.getText().toString(), System.currentTimeMillis()));
            ListRating item = new ListRating(mDataRating);
            refHotel_rating.child(mKey).setValue(new Gson().toJson(item));
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
        for (RatingModel item : mData) {
            score += Integer.parseInt(item.getScore());
        }
        return String.format("%.01f", (float) score / mData.size());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusBooking mData) {
        if (mData.action.equals(Constant.ACTION_RATING)) {
            if (mDataRating.size() == 0)
                mDataRating.addAll(((BookingCommentParrent) getParentFragment()).mDataRating.rating);
            avLoading.setVisibility(View.GONE);
            tvScore.setText(toCountScore(mDataRating));
        }
        EventBus.getDefault().removeStickyEvent(mData);
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

    @OnClick(R.id.fragHomeDetail_tvScore)
    public void onViewClicked() {
        if (avLoading.getVisibility() != View.VISIBLE) {
            RatingDialog ratingDialog = new RatingDialog(getContext(), mDataRating);
            ratingDialog.show();
        }
    }
}
