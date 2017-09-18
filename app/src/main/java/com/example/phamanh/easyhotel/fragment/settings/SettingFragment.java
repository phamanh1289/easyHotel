package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SettingFragment extends BaseFragment {

    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        KeyboardUtils.hideSoftKeyboard(getActivity());
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_setting));
        unbinder = ButterKnife.bind(this, view);
        ivBack.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragSetting_llProfile, R.id.fragSetting_llFAQ, R.id.fragSetting_llLogout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragSetting_llProfile:
                addFragment(new ProfileFragment(), true);
                break;
            case R.id.fragSetting_llFAQ:
                addFragment(new FAQFragment(), true);
                break;
            case R.id.fragSetting_llLogout:
                break;
        }
    }
}
