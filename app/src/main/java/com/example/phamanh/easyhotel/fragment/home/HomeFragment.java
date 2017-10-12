package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.activity.LoginActivity;
import com.example.phamanh.easyhotel.adapter.HotelMainAdapter;
import com.example.phamanh.easyhotel.base.BaseApplication;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.model.HotelModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.ListComment;
import com.example.phamanh.easyhotel.model.ListRating;
import com.example.phamanh.easyhotel.model.Location;
import com.example.phamanh.easyhotel.model.RatingModel;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;
    @BindView(R.id.fragHome_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;

    private List<InfomationModel> mDataInfomation = new ArrayList<>();
    private HotelMainAdapter adapter;
    private HotelModel hotel = new HotelModel();
    private List<CommentModel> mDataComment = new ArrayList<>();
    private ListComment mListComment = new ListComment();
    private List<RatingModel> mDataRating = new ArrayList<>();
    private ListRating mListRating = new ListRating();
    private ServiceDetailModel service = new ServiceDetailModel();
    private InfomationModel info = new InfomationModel();
    private List<String> mDataImage = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private LoginActivity loginActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        KeyboardUtils.hideSoftKeyboard(getActivity());
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_home));
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
        adapter = new HotelMainAdapter(mDataInfomation);
        adapter.setItemListener(toClickItem);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        showLoading();
        if (getUser() == null) {
            toGetDataProfile();
        }
        toGetDataHotel();
//        toAddDataDemo();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                        if (userModel.status) {
                            AppUtils.showAlert(getActivity(), "You have locked your account!", toExit);
                        } else {
                            BaseApplication application = (BaseApplication) getActivity().getApplication();
                            application.setCustomer(userModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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

    DialogListener toExit = new DialogListener() {
        @Override
        public void onConfirmClicked() {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
            } else {
                mGoogleApiClient.connect();
            }
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
            FirebaseAuth.getInstance().signOut();
            SharedPrefUtils.removeLogout(getActivity());
            BaseApplication application = (BaseApplication) getActivity().getApplication();
            application.setCustomer(null);
            dismissLoading();
            StartActivityUtils.toLogin(getActivity());
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onCancelClicked() {

        }
    };

    ItemListener toClickItem = pos -> addFragment(BookingCommentParrent.newInstance(mDataInfomation.get(pos), pos), true);

    private void toGetDataHotel() {
        if (mDataInfomation.size() != 0)
            mDataInfomation.clear();
        refHotel.addChildEventListener(toGetHotel);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            refHotel.removeEventListener(toGetHotel);
        } catch (Exception ignored) {
        }
    }

    ChildEventListener toGetHotel = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            info = new InfomationModel();
            info.setId(dataSnapshot.getKey());
            info.setPrice(dataSnapshot.child("price").getValue().toString());
            info.setName(dataSnapshot.child("name").getValue().toString());
            info.setLogo(dataSnapshot.child("logo").getValue().toString());
            info.setDescription(dataSnapshot.child("description").getValue().toString());
            info.setAddress(dataSnapshot.child("address").getValue().toString());
            info.setLocation(new Location(dataSnapshot.child("location").child("lat").getValue().toString(), dataSnapshot.child("location").child("lng").getValue().toString()));
            info.setDataImage((List<String>) dataSnapshot.child("mDataImage").getValue());
            mDataInfomation.add(info);
            adapter.notifyItemInserted(mDataInfomation.size());
            dismissLoading();
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
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        unbinder.unbind();
    }
}
