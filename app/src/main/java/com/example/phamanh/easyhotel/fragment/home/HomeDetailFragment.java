package com.example.phamanh.easyhotel.fragment.home;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.other.view.RatingDialog;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.RotationRatingBar;

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
    TextView tvScore;

    private HotelModel mHotelModel;
    private int rating;

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
        mHotelModel = ((BookingCommentParrent) getParentFragment()).mHotelModel;
        if (mHotelModel != null) {
            tvTitle.setText(mHotelModel.getInfomationModel().getName());
            tvDescription.setText(mHotelModel.getInfomation().getDescription());
            ivBanner.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(mHotelModel.getInfomation().getLogo(), Base64.DEFAULT), 0, Base64.decode(mHotelModel.getInfomation().getLogo(), Base64.DEFAULT).length));
        }
        ratingStar.setEmptyDrawableRes(R.drawable.ic_no_start);
        ratingStar.setFilledDrawableRes(R.drawable.ic_start);
        ratingStar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                rating = (int) v;
                AppUtils.showAlert(getContext(), getString(R.string.complete), "Would you like to rating " + rating + " for the hotel ?", toRating);
            }
        });
    }

    DialogListener toRating = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            tvScore.setText(String.valueOf(rating));
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


    @OnClick(R.id.fragHomeDetail_tvScore)
    public void onViewClicked() {
        RatingDialog ratingDialog = new RatingDialog(getContext(), mHotelModel.getDataRating());
        ratingDialog.show();
    }
}
