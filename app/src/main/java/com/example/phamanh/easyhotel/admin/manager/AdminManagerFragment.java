package com.example.phamanh.easyhotel.admin.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseApplication;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.SharedPrefUtils;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AdminManagerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.actionbar_imgBack)
    FrameLayout imgBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_admin, container, false);
        setActionBar(view, "Admin setting");
        unbinder = ButterKnife.bind(this, view);
        imgBack.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragSettingAdmin_llChangePass, R.id.fragSettingAdmin_llLogout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragSettingAdmin_llChangePass:
                addFragment(new ChangePasswordAdminFragment(), true);
                break;
            case R.id.fragSettingAdmin_llLogout:
                AppUtils.showAlertConfirm(getActivity(), "Do you want exit EasyHotel ?", toConfirmExit);
                break;
        }
    }

    DialogListener toConfirmExit = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            showLoading();
            logoutAccount();
        }

        @Override
        public void onCancelClicked() {

        }
    };

    private void logoutAccount() {
        FirebaseAuth.getInstance().signOut();
        SharedPrefUtils.removeLogout(getActivity());
        BaseApplication application = (BaseApplication) getActivity().getApplication();
        application.setCustomer(null);
        application.setRole(null);
        dismissLoading();
        StartActivityUtils.toLogin(getActivity());
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getActivity().finish();
    }
}
