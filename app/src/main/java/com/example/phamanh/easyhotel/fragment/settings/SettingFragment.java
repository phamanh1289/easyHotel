package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.activity.LoginActivity;
import com.example.phamanh.easyhotel.base.BaseApplication;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.example.phamanh.easyhotel.utils.SharedPrefUtils;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SettingFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;
    @BindView(R.id.fragSetting_llChangePass)
    FrameLayout llChangePass;

    private LoginActivity loginActivity;
    private GoogleApiClient mGoogleApiClient;
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
        init();
        return view;
    }

    private void init() {
        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(AppInvite.API)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        if (SharedPrefUtils.getString(getContext(), Constant.LOGIN_SOCIAL).length() != 0)
            llChangePass.setVisibility(View.GONE);
    }

    public void LogOutAndLoginHere() {
        FirebaseAuth.getInstance().signOut();
        SharedPrefUtils.removeLogout(getActivity());
        BaseApplication application = (BaseApplication) getActivity().getApplication();
        application.setCustomer(null);
        dismissLoading();
        StartActivityUtils.toLogin(getActivity());
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getActivity().finish();
    }

    private void logoutAccount() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        } else {
            mGoogleApiClient.connect();
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragSetting_llProfile, R.id.fragSetting_llFAQ, R.id.fragSetting_llLogout, R.id.fragSetting_llChangePass, R.id.fragSetting_llHistory, R.id.fragSetting_llFavourites})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragSetting_llProfile:
                addFragment(new ProfileFragment(), true);
                break;
            case R.id.fragSetting_llFAQ:
                addFragment(new FAQFragment(), true);
                break;
            case R.id.fragSetting_llLogout:
                showLoading();
                logoutAccount();
                LogOutAndLoginHere();
                break;
            case R.id.fragSetting_llChangePass:
                addFragment(new ChangePasswordFragment(), true);
                break;
            case R.id.fragSetting_llHistory:
                addFragment(new HistoryFragment(), true);
                break;
            case R.id.fragSetting_llFavourites:
                addFragment(new FavouritesFragment(), true);
                break;
        }
    }
}
