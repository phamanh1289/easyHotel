package com.example.phamanh.easyhotel.fragment.news;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NewDetailFragment extends BaseFragment {
    @BindView(R.id.actionbar_tvTitle)
    TextView tvTitle;
    @BindView(R.id.actionbar_imgBack)
    FrameLayout imgBack;
    @BindView(R.id.fragNewDetail_wvNew)
    WebView wvNew;
    Unbinder unbinder;
    private String mLink = "";

    public static NewDetailFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString("link", link);
        NewDetailFragment fragment = new NewDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "News Detail");
        setVisibilityTabBottom(View.GONE);
        init();
        getBundle();
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        wvNew.getSettings().setLoadsImagesAutomatically(true);
        wvNew.getSettings().setJavaScriptEnabled(true);
        wvNew.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }

    private void getBundle() {
        if (getArguments() != null && getArguments().containsKey("link")) {
            mLink = getArguments().getString("link");
            showLoading();
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    dismissLoading();
                }
            }.start();
            wvNew.loadUrl(mLink);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
