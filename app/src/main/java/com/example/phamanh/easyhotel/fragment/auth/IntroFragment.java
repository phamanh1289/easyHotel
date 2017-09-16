package com.example.phamanh.easyhotel.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IntroFragment extends BaseFragment {


    @BindView(R.id.fragIntro_ivBanner)
    ImageView ivBanner;
    @BindView(R.id.fragIntro_tvTitle)
    TextView tvTitle;
    Unbinder unbinder;


    public static IntroFragment newInstance(String title, int image) {

        Bundle args = new Bundle();
        args.putString(Constant.TITLE_INTRO, title);
        args.putSerializable(Constant.IMAGE_INTRO, image);
        IntroFragment fragment = new IntroFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tvTitle.setText(bundle.getString(Constant.TITLE_INTRO));
            Glide.with(getContext()).load(bundle.getInt(Constant.IMAGE_INTRO)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivBanner);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
