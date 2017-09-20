package com.example.phamanh.easyhotel.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.activity.LoginActivity;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.example.phamanh.easyhotel.utils.SharedPrefUtils;
import com.example.phamanh.easyhotel.utils.StartActivityUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class SignUpFragment extends BaseFragment {

    @BindView(R.id.fragSignUp_etEmail)
    EditText etEmail;
    @BindView(R.id.fragSignUp_etPassword)
    EditText etPassword;
    @BindView(R.id.fragSingUp_etConfirmPassword)
    EditText etConfirmPassword;

    private LoginActivity loginActivity;
    private boolean isShowPass;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        KeyboardUtils.setupUI(view, getActivity());
        return view;
    }

    private void init() {
        loginActivity = (LoginActivity) getActivity();
        initiateFacebook();
    }

    public void initiateFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getInformationLoginResult(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                }
        );
    }

    public void handleSignInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !TextUtils.isEmpty(accessToken.getUserId())) {
            getInformationLoginResult(accessToken);
        } else
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_birthday"));
    }

    private void getInformationLoginResult(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (object, response) -> {
                    showLoading();
//                    String id = accessToken.getUserId();
//                    String name = object.has("name") ? object.optString("name") : "";
//                    String image = "https://graph.facebook.com/" + id + "/picture?type=large";
//                    String email = object.has("email") ? object.optString("email") : "";
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
        handleFacebookAccessToken(accessToken);
    }

    public void handleSignInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(loginActivity.mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount signInAccount = result.getSignInAccount();
            assert signInAccount != null;
            String id = signInAccount.getId();
            SharedPrefUtils.setString(getContext(), Constant.ID, id);
//            String name = signInAccount.getDisplayName();
//            String email = signInAccount.getEmail();
//            String token = signInAccount.getIdToken();
//            String image = String.valueOf(signInAccount.getPhotoUrl());
            showLoading();
        }
    }

    GoogleApiClient.OnConnectionFailedListener failedGoogleListener = connectionResult -> {
    };

    public boolean checkValidInput() {
        String strError = "";
        if (etEmail.getText().toString().isEmpty())
            strError = "Please enter your email.";
        else if (!AppUtils.isValidEmail(etEmail.getText().toString()))
            strError = "Email is incorrect. Please try again.";
        else if (etPassword.getText().toString().isEmpty())
            strError = "Please input password.";
        else if (etPassword.getText().length() < 6)
            strError = "Please input at least 6 characters.";
        else if (etConfirmPassword.getText().toString().isEmpty())
            strError = "Please input confirm password.";
        else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
            strError = "The passwords you entered do not match. Please try again.";
        if (TextUtils.isEmpty(strError))
            return true;
        else {
            AppUtils.showAlert(getActivity(), getString(R.string.error), strError, null);
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.fragSignUp_tvLogin, R.id.fragSignUp_ivFacebook, R.id.fragSignUp_ivGoogle, R.id.fragSignUp_tvSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragSignUp_tvLogin:
                if (checkValidInput())
                    toCreateNewUser();
//                addFragment(new SignUpStep2Fragment(), true);
                break;
            case R.id.fragSignUp_ivFacebook:
                showLoading();
                handleSignInFacebook();
                break;
            case R.id.fragSignUp_ivGoogle:
                showLoading();
                handleSignInGoogle();
                break;
            case R.id.fragSignUp_tvSignUp:
                addFragment(new LoginFragment(), false);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void toProcessLogin() {
        toAddReLogin();
        dismissLoading();
        StartActivityUtils.toMain(getActivity(), null);
    }

    private void toCreateNewUser() {
        showLoading();
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toSendMailVerify();
                        } else {
                            AppUtils.showAlert(getContext(), getString(R.string.error), task.getException().getMessage(), null);
                            dismissLoading();
                        }
                    }
                });
    }

    DialogListener toCLickMoveLogin = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            dismissLoading();
            addFragment(new LoginFragment(), false);
        }

        @Override
        public void onCancelClicked() {

        }
    };

    private void toSendMailVerify() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AppUtils.showAlert(getContext(), getString(R.string.complete), "Thank you for signing up. Please log in to your email to verify your account.", toCLickMoveLogin);
                        }
                    }
                });
    }

    private boolean toCheckVerify(FirebaseUser user) {
        return user.isEmailVerified();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        toProcessLogin();
                    } else {
                        AppUtils.showAlert(getContext(), getString(R.string.error), task.getException().getMessage(), null);
                        dismissLoading();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        toProcessLogin();
                    } else {
                        AppUtils.showAlert(getContext(), getString(R.string.error), task.getException().getMessage(), null);
                        dismissLoading();
                    }
                });
    }

    private void toAddReLogin() {
        SharedPrefUtils.saveLogin(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        StartActivityUtils.toMain(getActivity(), null);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


}
