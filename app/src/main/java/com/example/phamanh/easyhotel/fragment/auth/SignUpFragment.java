package com.example.phamanh.easyhotel.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.activity.LoginActivity;
import com.example.phamanh.easyhotel.base.BaseApplication;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.UserModel;
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
        else if (etConfirmPassword.getText().toString().trim().isEmpty())
            strError = "Please input confirm password.";
        else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
            strError = "The passwords you entered do not match. Please try again.";
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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
        dismissLoading();
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
                if (checkValidInput() && AppUtils.isNetworkAvailable(getContext()))
                    toCreateNewUser();
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


    private void toCreateNewUser() {
        showLoading();
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        toSendMailVerify();
                    } else {
                        AppUtils.showAlert(getContext(), task.getException().getMessage(), null);
                        dismissLoading();
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AppUtils.showAlert(getContext(), "Thank you for signing up. Please log in to your email to verify your account.", toCLickMoveLogin);
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
                        toAddReLogin();
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
                        toAddReLogin();
                    } else {
                        AppUtils.showAlert(getContext(), task.getException().getMessage(), null);
                        dismissLoading();
                    }
                });
    }

    private void toAddReLogin() {
        if (getUser() == null)
            refMember.child(mUser.getUid()).setValue(new Gson().toJson(new UserModel(mUser.getEmail(), "Male", "", "", "", "", Constant.IMAGE_DEFAULT)));
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (getUser() == null)
                    refMember.child(mUser.getUid()).setValue(new Gson().toJson(new UserModel(mUser.getEmail(), "Male", "", "", "", "", Constant.IMAGE_DEFAULT)));
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
