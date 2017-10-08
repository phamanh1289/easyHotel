package com.example.phamanh.easyhotel.admin.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.example.phamanh.easyhotel.utils.SharedPrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChangePasswordAdminFragment extends BaseFragment {

    @BindView(R.id.fragChangePasswordAdmin_etOldPass)
    EditText etOldPass;
    @BindView(R.id.fragChangePasswordAdmin_etNewPass)
    EditText etNewPass;
    @BindView(R.id.fragChangePasswordAdmin_etConfirmPass)
    EditText etConfirmPass;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password_admin, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, "Change password admin");
        setVisibilityTabBottom(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public boolean checkValidInput() {
        String strError = "";
        String oldPass = SharedPrefUtils.getString(getContext(), Constant.PASSWORD);
        if (etOldPass.getText().toString().isEmpty())
            strError = "Please enter your current password.";
        else if (!oldPass.equals(etOldPass.getText().toString()))
            strError = "Old password and Current password not the same.";
        else if (etNewPass.getText().toString().isEmpty())
            strError = "Please enter your new password.";
        else if (etConfirmPass.getText().toString().isEmpty())
            strError = "Please enter confirm password";
        else if (etOldPass.getText().toString().equals(etNewPass.getText().toString()))
            strError = "Current password and New password the same.";
        else if (!etNewPass.getText().toString().equals(etConfirmPass.getText().toString()))
            strError = "New password and Confirm password not the same.";
        if (TextUtils.isEmpty(strError))
            return true;
        else {
            AppUtils.showAlert(getActivity(), strError, null);
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragChangePasswordAdmin_tvSubmit)
    public void onViewClicked() {
        if (checkValidInput()) {
            showLoading();
            mUser.updatePassword(etNewPass.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SharedPrefUtils.setString(getContext(), Constant.PASSWORD, etNewPass.getText().toString());
                            AppUtils.showAlert(getContext(), "Change password successfully.", null);
                            dismissLoading();
                            getActivity().onBackPressed();
                        }
                    });
        }
    }
}
