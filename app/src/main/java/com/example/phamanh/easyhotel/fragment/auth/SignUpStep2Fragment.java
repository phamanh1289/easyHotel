package com.example.phamanh.easyhotel.fragment.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SignUpStep2Fragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.fragForgotPassword_evEmail)
    EditText evEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_step_2, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragForgotPassword_tvSubmit, R.id.fragForgotPassword_tvBackLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragForgotPassword_tvSubmit:
                if (AppUtils.isValidEmail(evEmail.getText().toString())) {
                    if (evEmail.getText().toString().equals(getString(R.string.email_admin))) {
                        AppUtils.showAlert(getContext(), getString(R.string.error), "Admin not request reset password.", null);
                    } else {
                        showLoading();
                        mAuth.sendPasswordResetEmail(evEmail.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            AppUtils.showAlert(getContext(), getString(R.string.complete), "Your password has been sent to you. Please check your email.", null);
                                            dismissLoading();
                                            getActivity().onBackPressed();
                                        } else {
                                            AppUtils.showAlert(getContext(), getString(R.string.error), "Send request failed.", null);
                                            dismissLoading();
                                        }
                                    }
                                });
                    }
                } else
                    AppUtils.showAlert(getContext(), getString(R.string.error), "Email is incorrect. Please try again.", null);
                break;
            case R.id.fragForgotPassword_tvBackLogin:
                getActivity().onBackPressed();
                break;
        }
    }
}
