package com.example.phamanh.easyhotel.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.View;

import com.example.phamanh.easyhotel.activity.MainActivity;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseFragment extends Fragment {

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseUser mUser = mAuth.getCurrentUser();
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference refMember = firebaseDatabase.getReference("member");
    public DatabaseReference refBooking = firebaseDatabase.getReference("booking");
    public DatabaseReference refHotel = firebaseDatabase.getReference("hotel");


    public void setActionBar(View view, String title) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setActionBar(view, title);
        }
    }

    public void clearAllBackStack() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).clearAllBackStack();
        }
    }

    public void replaceFragment(BaseFragment fragment, boolean isAddToBackStack) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).replaceFragment(fragment, isAddToBackStack);
        }
    }

    public void setVisibilityTabBottom(int visibility) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).llTabBottom.setVisibility(visibility);
        }
    }

    public void openTabLocation(int index) {
        Intent intent = new Intent().setClass(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constant.INDEX_TAB_LOCATION_KEY, index);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void addFragment(BaseFragment fragment, boolean isAddToBackStack) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).addFragment(fragment, isAddToBackStack);
        }
    }

    public void showLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }
    }

    public void dismissLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissLoading();
        }
    }

    public UserModel getUser() {
        if (getActivity() != null && AppUtils.isNetworkAvailable(getActivity())) {
            BaseApplication application = (BaseApplication) getActivity().getApplication();
            return application.getUser();
        } else
            return new UserModel();
    }
}
