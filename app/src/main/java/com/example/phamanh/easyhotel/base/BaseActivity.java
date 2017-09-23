package com.example.phamanh.easyhotel.base;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.activity.MainActivity;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.other.view.LodingDialog;
import com.example.phamanh.easyhotel.utils.AppUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class BaseActivity extends AppCompatActivity {

    private LodingDialog mDialogView;

    public void showLoading() {
        if (AppUtils.isNetworkAvailable(this))
            if (mDialogView != null) {
                mDialogView.show();
            } else {
                mDialogView = new LodingDialog(this);
                mDialogView.setCanceledOnTouchOutside(false);
                mDialogView.show();
            }
    }

    public void dismissLoading() {
        if (mDialogView != null) {
            mDialogView.dismiss();
        }
    }

    public void setActionBar(View view, String title) {
        TextView tvTitle = (TextView) view.findViewById(R.id.actionbar_tvTitle);
        View vBack = view.findViewById(R.id.actionbar_imgBack);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (vBack != null) {
            vBack.setOnClickListener(onBackClick);
        }

    }

    View.OnClickListener onBackClick = v -> onBackPressed();

    private void addReplaceFragment(BaseFragment fragment, boolean isReplace, boolean isAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null && fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isReplace)
                fragmentTransaction.replace(this instanceof MainActivity ? android.R.id.tabcontent : R.id.frmContainer, fragment);
            else {
                android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentById(this instanceof MainActivity ? android.R.id.tabcontent : R.id.frmContainer);
                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment);
                }
                fragmentTransaction.add(this instanceof MainActivity ? android.R.id.tabcontent : R.id.frmContainer, fragment, fragment.getClass().getSimpleName());
            }
            if (isAddToBackStack) {
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            fragmentTransaction.commit();
        }
    }

    public void saveCurrentTab(int currentTab) {
        BaseApplication application = (BaseApplication) this.getApplication();
        application.currentTab = currentTab;
    }

    public void replaceFragment(BaseFragment fragment, boolean isAddToBackStack) {

        addReplaceFragment(fragment, true, isAddToBackStack);
    }

    public void addFragment(BaseFragment fragment, boolean isAddToBackStack) {
        addReplaceFragment(fragment, false, isAddToBackStack);
    }

    public void clearAllBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStack();
        }
    }

    public UserModel getUser() {
        if (AppUtils.isNetworkAvailable(this)) {
            BaseApplication application = (BaseApplication) this.getApplication();
            return application.getUser();
        } else
            return new UserModel();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
