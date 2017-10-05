package com.example.phamanh.easyhotel.admin.hotel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.ChooseImageAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.other.view.ChooseImageDialog;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.ImageOrientation;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.phamanh.easyhotel.utils.AppUtils.calculateInSampleSize;


public class AddUserFragment extends BaseFragment {

    public final int RQ_SELECT_PHOTO = 1;
    public final int RQ_SELECT_MORE = 4;
    public final int REQUEST_IMAGE_CAPTURE = 2;
    public final int MY_PERMISSIONS_REQUEST_READ_STORE = 3;

    @BindView(R.id.fragAddHotel_tvNameHotel)
    EditText tvName;
    @BindView(R.id.fragAddHotel_tvPrice)
    EditText tvPrice;
    @BindView(R.id.fragAddHotel_ivMale)
    ImageView ivMale;
    @BindView(R.id.fragAddHotel_tvMale)
    TextView tvMale;
    @BindView(R.id.fragAddHotel_ivFemale)
    ImageView ivFemale;
    @BindView(R.id.fragAddHotel_tvFemale)
    TextView tvFemale;
    @BindView(R.id.fragAddHotel_tvAddress)
    EditText tvAddress;
    @BindView(R.id.fragAddHotel_tvLocation)
    EditText tvLocation;
    @BindView(R.id.fragAddHotel_rvMain)
    RecyclerView rvMain;
    @BindView(R.id.fragAddHotel_tvDescription)
    EditText tvDescription;
    @BindView(R.id.fragAddHotel_ivBanner)
    RoundedImageView ivBanner;

    private Bitmap bitmapChoice;
    private List<Bitmap> mDataImage = new ArrayList<>();
    private ChooseImageAdapter mAdapter;
    private ChooseImageDialog mImageDialog;

    Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "Add Hotel");
        KeyboardUtils.setupUI(view, getActivity());
        init();
        return view;
    }

    private void init() {
        mAdapter = new ChooseImageAdapter(mDataImage);
        mAdapter.setItemListener(toChoiceImage);
        rvMain.setAdapter(mAdapter);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvMain.setLayoutManager(layout);
    }

    ItemListener toChoiceImage = new ItemListener() {
        @Override
        public void onItemClicked(int pos) {
            mDataImage.remove(pos);
            mAdapter.notifyItemRemoved(pos);
        }
    };

    private void toOpenDialogImage() {
        if (mImageDialog == null)
            mImageDialog = new ChooseImageDialog(getActivity());
        mImageDialog.setDialogListener(new DialogListener() {

            @Override
            public void onConfirmClicked() {
                takePictureIntent();
                mImageDialog.dismiss();
            }

            @Override
            public void onCancelClicked() {
                pickImage();
                mImageDialog.dismiss();
            }
        });
        mImageDialog.show();
    }

//    public boolean isValidate() {
//        boolean isCheck = true;
//        if (tvUserName.getText().toString().trim().isEmpty()) {
//            tvUserName.setHintTextColor(Color.RED);
//            isCheck = false;
//        }
//        if (tvDOB.getText().toString().trim().isEmpty()) {
//            tvDOB.setHintTextColor(Color.RED);
//            isCheck = false;
//        }
//        if (tvMobilePhone.getText().toString().trim().isEmpty()) {
//            tvMobilePhone.setHintTextColor(Color.RED);
//            isCheck = false;
//        }
//        if (tvEmail.getText().toString().trim().isEmpty()) {
//            tvEmail.setHintTextColor(Color.RED);
//            isCheck = false;
//        }
//        if (tvAddress.getText().toString().trim().isEmpty()) {
//            tvAddress.setHintTextColor(Color.RED);
//            isCheck = false;
//        }
//        return isCheck;
//    }
//
//    public boolean checkValidInput() {
//        String strError = "";
//        if (tvUserName.getText().toString().trim().isEmpty())
//            strError = "Please input full name.";
//        else if (tvDOB.getText().toString().trim().isEmpty())
//            strError = "Please indicate date of birth.";
//        else if (tvMobilePhone.getText().toString().trim().isEmpty())
//            strError = "Please input mobile phone.";
//        else if (tvAddress.getText().toString().trim().isEmpty())
//            strError = "Please input address.";
//        else if (tvEmail.getText().toString().trim().isEmpty())
//            strError = "Please input email.";
//        if (TextUtils.isEmpty(strError))
//            return true;
//        else {
//            AppUtils.showAlert(getActivity(),  strError, null);
//            return false;
//        }
//    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_STORE);
        } else
            startActivityForResult(intent, RQ_SELECT_PHOTO);
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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
                mDataImage.add(AppUtils.getResizedBitmap(bitmapNew, 1080));
                mAdapter.notifyItemInserted(mDataImage.size() - 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mDataImage.add(AppUtils.getResizedBitmap(imageBitmap, 1080));
            mAdapter.notifyItemInserted(mDataImage.size() - 1);
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
                    AppUtils.showAlert(getContext(), "Permission is required for getting list of files.", null);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fragAddHotel_ivBanner, R.id.fragAddHotel_llSingle, R.id.fragAddHotel_llDouble, R.id.fragAddHotel_tvLocation, R.id.fragAddHotel_tvSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragAddHotel_ivBanner:
                toOpenDialogImage();
                break;
            case R.id.fragAddHotel_llSingle:
                break;
            case R.id.fragAddHotel_llDouble:
                break;
            case R.id.fragAddHotel_tvLocation:
                break;
            case R.id.fragAddHotel_tvSubmit:
                break;
        }
    }
}
