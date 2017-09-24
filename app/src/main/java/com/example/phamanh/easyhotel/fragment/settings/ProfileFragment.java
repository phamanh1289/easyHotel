package com.example.phamanh.easyhotel.fragment.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.model.UserModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.ImageOrientation;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.phamanh.easyhotel.utils.AppUtils.calculateInSampleSize;


public class ProfileFragment extends BaseFragment {

    @BindView(R.id.fragProfile_tvUserName)
    EditText tvUserName;
    @BindView(R.id.fragProfile_tvDOB)
    EditText tvDOB;
    @BindView(R.id.fragProfile_tvEmail)
    EditText tvEmail;
    @BindView(R.id.fragProfile_tvMale)
    TextView tvMale;
    @BindView(R.id.fragProfile_tvFemale)
    TextView tvFemale;
    @BindView(R.id.fragProfile_tvAddress)
    EditText tvAddress;
    @BindView(R.id.fragProfile_tvMobilePhone)
    EditText tvMobilePhone;
    Unbinder unbinder;
    @BindView(R.id.fragProfile_ivMale)
    ImageView ivMale;
    @BindView(R.id.fragProfile_ivFemale)
    ImageView ivFemale;
    @BindView(R.id.fragProfile_ivBanner)
    RoundedImageView ivBanner;

    public final int RQ_SELECT_PHOTO = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_STORE = 2;
    private String imgBitMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setActionBar(view, getString(R.string.my_profile));
        setVisibilityTabBottom(View.GONE);
        KeyboardUtils.setupUI(view, getActivity());
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        showLoading();
        handleSexSelected(true);
        toGetProfile();
    }

    private void handleSexSelected(boolean isMale) {
        ivMale.setSelected(isMale);
        tvMale.setSelected(isMale);
        ivFemale.setSelected(!isMale);
        tvFemale.setSelected(!isMale);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void toPostUpdateProfile() {
        refMember.child(mUser.getUid()).setValue(new Gson().toJson(new UserModel(tvEmail.getText().toString(), tvMale.isSelected() ? getString(R.string.male) : getString(R.string.female), tvUserName.getText().toString(),
                tvDOB.getText().toString(), tvAddress.getText().toString(), tvMobilePhone.getText().toString(), imgBitMap)));
        dismissLoading();
        AppUtils.showAlert(getContext(), getString(R.string.complete), "Update successfully.", null);
    }

    private void toGetProfile() {
        tvEmail.setText(getUser().getEmail());
        tvUserName.setText(getUser().getFullName());
        tvDOB.setText(getUser().getDob());
        tvAddress.setText(getUser().getAddress());
        tvMobilePhone.setText(getUser().getPhone());
        if (!getUser().getAvatar().equals(""))
            ivBanner.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(getUser().getAvatar(), Base64.DEFAULT), 0, Base64.decode(getUser().getAvatar(), Base64.DEFAULT).length));
        handleSexSelected(getUser().getGender().equals(getString(R.string.male)));
        dismissLoading();
    }

    public boolean isValidate() {
        boolean isCheck = true;
        if (tvUserName.getText().toString().isEmpty()) {
            tvUserName.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvDOB.getText().toString().isEmpty()) {
            tvDOB.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvMobilePhone.getText().toString().isEmpty()) {
            tvMobilePhone.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvEmail.getText().toString().isEmpty()) {
            tvEmail.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvAddress.getText().toString().isEmpty()) {
            tvAddress.setHintTextColor(Color.RED);
            isCheck = false;
        }
        return isCheck;
    }

    public boolean checkValidInput() {
        String strError = "";
        if (tvUserName.getText().toString().isEmpty())
            strError = "Please input full name.";
        else if (tvDOB.getText().toString().isEmpty())
            strError = "Please indicate date of birth.";
        else if (tvMobilePhone.getText().toString().isEmpty())
            strError = "Please input mobile phone.";
        else if (tvAddress.getText().toString().isEmpty())
            strError = "Please input address.";
        else if (tvEmail.getText().toString().isEmpty())
            strError = "Please input email.";
        if (TextUtils.isEmpty(strError))
            return true;
        else {
            AppUtils.showAlert(getActivity(), getString(R.string.error), strError, null);
            return false;
        }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORE);
        } else
            startActivityForResult(intent, RQ_SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_SELECT_PHOTO) {
            if (data == null) return;
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                int resolution = 500;
                options.inSampleSize = calculateInSampleSize(options, resolution, resolution);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                Bitmap bitmapNew = ImageOrientation.modifyOrientation(getActivity(), bitmap, uri);
                ivBanner.setImageBitmap(AppUtils.getResizedBitmap(bitmapNew, 1080));
                imgBitMap = AppUtils.toChangeBitmap(bitmapNew);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.fragProfile_llMale, R.id.fragProfile_llFemale, R.id.fragProfile_tvSubmit, R.id.fragProfile_tvDOB, R.id.fragProfile_ivBanner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragProfile_llMale:
                handleSexSelected(true);
                break;
            case R.id.fragProfile_llFemale:
                handleSexSelected(false);
                break;
            case R.id.fragProfile_tvSubmit:
                if (isValidate()) {
                    showLoading();
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            toPostUpdateProfile();
                        }
                    }.start();
                } else checkValidInput();
                break;
            case R.id.fragProfile_tvDOB:
                AppUtils.showPickTime(getContext(), tvDOB, false);
                break;
            case R.id.fragProfile_ivBanner:
                pickImage();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(getActivity(), "Permission is required for getting list of files", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
