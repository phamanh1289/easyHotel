package com.example.phamanh.easyhotel.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.activity.LoginActivity;
import com.example.phamanh.easyhotel.base.BaseApplication;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.other.enums.RoleEnum;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LoginFragment extends BaseFragment {

    @BindView(R.id.fragLogin_tvLogin)
    TextView tvLogin;
    @BindView(R.id.fragLogin_etPassword)
    EditText etPassword;
    @BindView(R.id.fragLogin_etEmail)
    EditText etEmail;

    private LoginActivity loginActivity;
    private boolean isShowPass;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;

    private UserModel mUserModel;

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        KeyboardUtils.setupUI(view, getActivity());
        init();
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
            showLoading();
        }
    }

    GoogleApiClient.OnConnectionFailedListener failedGoogleListener = connectionResult -> {
    };

    public boolean checkValidInput() {
        String strError = "";
        if (etEmail.getText().toString().trim().isEmpty())
            strError = "Please enter your email.";
        else if (!AppUtils.isValidEmail(etEmail.getText().toString()))
            strError = "Email is incorrect. Please try again.";
        else if (etPassword.getText().toString().trim().isEmpty())
            strError = "Please input password.";
        else if (etPassword.getText().length() < 6)
            strError = "Please input at least 6 characters.";
        if (TextUtils.isEmpty(strError))
            return true;
        else {
            AppUtils.showAlert(getActivity(), strError, null);
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    @OnClick({R.id.fragLogin_ivShowPass, R.id.fragLogin_tvLogin, R.id.fragLogin_ivFacebook, R.id.fragLogin_ivGoogle, R.id.fragLogin_tvSignUp, R.id.fragLogin_tvForgot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragLogin_ivShowPass:
                etPassword.setInputType(!isShowPass ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isShowPass = !isShowPass;
                break;
            case R.id.fragLogin_tvLogin:
                if (checkValidInput() && AppUtils.isNetworkAvailable(getContext())) {
                    showLoading();
                    toLoginUser();
                }
                break;
            case R.id.fragLogin_ivFacebook:
                showLoading();
                handleSignInFacebook();
                break;
            case R.id.fragLogin_ivGoogle:
                showLoading();
                handleSignInGoogle();
                break;
            case R.id.fragLogin_tvSignUp:
                addFragment(new SignUpFragment(), false);
                break;
            case R.id.fragLogin_tvForgot:
                addFragment(new SignUpStep2Fragment(), true);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void toLoginUser() {
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified() || FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(Constant.MAIL_ADMIN)) {
                            mUser = FirebaseAuth.getInstance().getCurrentUser();
                            toGetDataProfile();
                            if (!mUser.getEmail().equals(Constant.MAIL_ADMIN))
                                toAddReLogin(true);
                            else
                                SharedPrefUtils.setString(getActivity(), Constant.MAIL_ADMIN,Constant.MAIL_ADMIN);
                        } else {
                            dismissLoading();
                            AppUtils.showAlert(getContext(), "Email not activated", null);
                        }
                    } else {
                        AppUtils.showAlert(getContext(), task.getException().getMessage(), null);
                        dismissLoading();
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
                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        toGetDataProfile();
                        toAddReLogin(false);
                    } else {
                        AppUtils.showAlert(getContext(), task.getException().getMessage(), null);
                        dismissLoading();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        toGetDataProfile();
                        toAddReLogin(false);
                    } else {
                        AppUtils.showAlert(getContext(), task.getException().getMessage(), null);
                        dismissLoading();
                    }
                });
    }

    private void toAddReLogin(boolean check) {
        if (check)
            SharedPrefUtils.saveLogin(getActivity(), etEmail.getText().toString(), etPassword.getText().toString());
        else
            SharedPrefUtils.saveLoginSocial(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        dismissLoading();
        StartActivityUtils.toMain(getActivity(), null);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void toGetDataProfile() {
        refMember.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (mUser.getUid().equals(dataSnapshot.getKey())) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                        UserModel userModel = gson.fromJson(jsonObject.toString(), UserModel.class);
                        BaseApplication application = (BaseApplication) getActivity().getApplication();
                        application.setCustomer(userModel);
                        if (mUser.getEmail().equals(Constant.MAIL_ADMIN)) {
                            application.setRole(RoleEnum.ADMIN);
                            StartActivityUtils.toMain(getActivity(), null);
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (getUser() == null)
                    refMember.child(mUser.getUid()).setValue(new Gson().toJson(new UserModel(mUser.getEmail(), "Male", "", "", "", "", Constant.STORE + "member_" + mUser.getUid())));
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
        });

    }
}
