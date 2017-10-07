package com.example.phamanh.easyhotel.admin.hotel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.ChooseImageAdapter;
import com.example.phamanh.easyhotel.adapter.PlaceAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ChoiceImageListener;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.EventBusUpload;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.Location;
import com.example.phamanh.easyhotel.model.PlaceAutocomplete;
import com.example.phamanh.easyhotel.model.RoomModel;
import com.example.phamanh.easyhotel.model.ServiceDetailModel;
import com.example.phamanh.easyhotel.other.database.DataHardCode;
import com.example.phamanh.easyhotel.other.view.ChooseImageDialog;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.ImageOrientation;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.phamanh.easyhotel.utils.AppUtils.calculateInSampleSize;
import static com.example.phamanh.easyhotel.utils.Constant.STORE;


public class AddHotelFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

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
    AutoCompleteTextView tvAddress;
    @BindView(R.id.fragAddHotel_tvLocation)
    EditText tvLocation;
    @BindView(R.id.fragAddHotel_tvService)
    EditText tvService;
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
    private boolean isCheckLoadPhoto, isCheckChangePhoto;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAdapter mPlaceAdapter;
    private double lat, lng;
    private Location mLocation;
    private String id, imgImage;
    private Bitmap getPathImage;
    private int startImage, endImage;
    private SelectSinglePopup popupSingle, popupService;
    private List<String> mDataSingle = new ArrayList<>(), mDataService = new ArrayList<>();
    private ServiceDetailModel mServiceDetailModel = new ServiceDetailModel();
    private static ServiceDetailModel mAddService = new ServiceDetailModel();
    private InfomationModel info = new InfomationModel();
    private RoomModel mRoomModel = new RoomModel();

    Unbinder unbinder;

    public static AddHotelFragment newInstance(InfomationModel info, RoomModel book, ServiceDetailModel service) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.HOTEL_INFO, info);
        args.putSerializable(Constant.ROOM, book);
        args.putSerializable(Constant.HOTEL_SERVICE, service);
        AddHotelFragment fragment = new AddHotelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, "Register Hotel");
        KeyboardUtils.setupUI(view, getActivity());
        init();
        getBundle();
        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            info = (InfomationModel) bundle.getSerializable(Constant.HOTEL_INFO);
            mRoomModel = (RoomModel) bundle.getSerializable(Constant.ROOM);
            mServiceDetailModel = (ServiceDetailModel) bundle.getSerializable(Constant.HOTEL_SERVICE);
            toLoadData();
        }
    }

    private void init() {
        mAdapter = new ChooseImageAdapter(mDataImage);
        mAdapter.setItemListener(toChoiceImage);
        rvMain.setAdapter(mAdapter);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvMain.setLayoutManager(layout);
        setupAutoComplete();
        ivMale.setSelected(true);
        ivFemale.setSelected(true);
        if (mDataSingle.size() == 0)
            mDataSingle.addAll(DataHardCode.getRoomHotel());
        toGetDataService();
        id = "hotel_" + String.valueOf(System.currentTimeMillis());
    }

    private void toGetDataService() {
        showLoading();
        refHotel_service.child("data").addListenerForSingleValueEvent(toAddService);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                dismissLoading();
            }
        }.start();
    }

    ValueEventListener toAddService = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.getKey().equals("data")) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    if (jsonObject != null) {
                        mServiceDetailModel = gson.fromJson(jsonObject.toString(), ServiceDetailModel.class);
                        mDataService.addAll(mServiceDetailModel.getService());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        refHotel_service.removeEventListener(toAddService);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    public boolean isValidate() {
        boolean isCheck = true;
        if (tvName.getText().toString().trim().isEmpty()) {
            tvName.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvPrice.getText().toString().trim().isEmpty()) {
            tvPrice.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvMale.getText().toString().trim().isEmpty()) {
            tvMale.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvFemale.getText().toString().trim().isEmpty()) {
            tvFemale.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvAddress.getText().toString().trim().isEmpty()) {
            tvAddress.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvService.getText().toString().trim().isEmpty()) {
            tvService.setHintTextColor(Color.RED);
            isCheck = false;
        }
        if (tvDescription.getText().toString().trim().isEmpty()) {
            tvDescription.setHintTextColor(Color.RED);
            isCheck = false;
        }
        return isCheck;
    }

    public boolean checkValidInput() {
        String strError = "";
        if (!isCheckChangePhoto)
            strError = "Please indicate logo hotel.";
        else if (tvName.getText().toString().trim().isEmpty())
            strError = "Please input hotel name.";
        else if (tvPrice.getText().toString().trim().isEmpty())
            strError = "Please input price.";
        else if (tvMale.getText().toString().trim().isEmpty())
            strError = "Please indicate single room.";
        else if (tvFemale.getText().toString().trim().isEmpty())
            strError = "Please indicate double room.";
        else if (tvAddress.getText().toString().trim().isEmpty())
            strError = "Please input address.";
        else if (tvDescription.getText().toString().trim().isEmpty())
            strError = "Please input address.";
        else if (tvDescription.getText().toString().trim().isEmpty())
            strError = "Please indicate service.";
        else if (tvDescription.getText().toString().trim().isEmpty())
            strError = "Please input description.";
        if (TextUtils.isEmpty(strError))
            return true;
        else {
            AppUtils.showAlert(getActivity(), strError, null);
            return false;
        }
    }

    @OnClick({R.id.fragAddHotel_ivBanner, R.id.fragAddHotel_llSingle, R.id.fragAddHotel_llDouble, R.id.fragAddHotel_tvLocation, R.id.fragAddHotel_tvSubmit, R.id.fragAddHotel_tvAddPhoto, R.id.fragAddHotel_tvService})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragAddHotel_ivBanner:
                isCheckLoadPhoto = false;
                toOpenDialogImage();
                break;
            case R.id.fragAddHotel_llSingle:
                AppUtils.toGetPopup(getActivity(), view, popupSingle, mDataSingle, tvMale);
                break;
            case R.id.fragAddHotel_llDouble:
                AppUtils.toGetPopup(getActivity(), view, popupSingle, mDataSingle, tvFemale);
                break;
            case R.id.fragAddHotel_tvSubmit:
                if (!isValidate()) {
                    checkValidInput();
                } else
                    toAddNewHotel();
                break;
            case R.id.fragAddHotel_tvAddPhoto:
                isCheckLoadPhoto = true;
                toOpenDialogImage();
                break;
            case R.id.fragAddHotel_tvService:
                toAddDataService(getActivity(), view, popupService, mDataService, tvService);
                break;
        }
    }

    public static void toAddDataService(Context context, View view, SelectSinglePopup singlePopup, List<String> mData, TextView textView) {
        if (singlePopup == null) {
            singlePopup = new SelectSinglePopup(context, mData, false);
        }
        final String[] result = {""};
        SelectSinglePopup finalSinglePopup = singlePopup;
        singlePopup.setOnItemListener(pos -> {
            result[0] = mData.get(pos);
            mAddService.getService().add(result[0]);
            textView.setText(AppUtils.toAddString(textView.getText().toString(), result[0]));
            textView.setTextColor(ContextCompat.getColor(context, R.color.denimBlue));
            finalSinglePopup.dismiss();
        });
        singlePopup.showAsDropDown(view);
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
        mImageDialog.setDialogListener(new ChoiceImageListener() {

            @Override
            public void onTakePhotoClicked() {
                takePictureIntent();
                mImageDialog.dismiss();
            }

            @Override
            public void onChoicePhotoClicked() {
                pickImage();
                mImageDialog.dismiss();
            }
        });
        mImageDialog.show();
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
                if (isCheckLoadPhoto) {
                    mDataImage.add(AppUtils.getResizedBitmap(bitmapNew, 1080));
                    mAdapter.notifyItemInserted(mDataImage.size() - 1);
                } else {
                    getPathImage = AppUtils.getResizedBitmap(bitmapNew, 1080);
                    ivBanner.setImageBitmap(getPathImage);
                    isCheckChangePhoto = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (isCheckLoadPhoto) {
                mDataImage.add(AppUtils.getResizedBitmap(imageBitmap, 1080));
                mAdapter.notifyItemInserted(mDataImage.size() - 1);
            } else {
                getPathImage = AppUtils.getResizedBitmap(imageBitmap, 1080);
                ivBanner.setImageBitmap(getPathImage);
            }
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

    private void setupAutoComplete() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(getActivity(), Constant.GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();
        }
        tvAddress.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAdapter = new PlaceAdapter(getContext(), android.R.layout.simple_list_item_1);
        tvAddress.setAdapter(mPlaceAdapter);
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = places -> {
        if (!places.getStatus().isSuccess()) {
            return;
        }
        final Place place = places.get(0);
        LatLng location = place.getLatLng();
        lat = location.latitude;
        lng = location.longitude;
        tvLocation.setText(String.valueOf(lat) + " , " + String.valueOf(lng));
        mLocation = new Location(String.valueOf(lat), String.valueOf(lng));
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutocomplete item = mPlaceAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    private void toAddNewHotel() {
        showLoading();
        info = new InfomationModel();
        info.setAddress(tvAddress.getText().toString());
        info.setDescription(tvDescription.getText().toString());
        info.setName(tvName.getText().toString());
        if (info.getId() == null)
            info.setId(id);
        info.setPrice(tvPrice.getText().toString());
        info.setLocation(mLocation);
        toUploadImage(getPathImage);
    }

    private void toUploadImage(Bitmap bitmap) {
        baseStore = FirebaseStorage.getInstance().getReferenceFromUrl(info.getLogo() != null ? info.getLogo() : STORE + id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataImage = baos.toByteArray();
        UploadTask uploadTask = baseStore.putBytes(dataImage);
        uploadTask.addOnFailureListener(exception -> AppUtils.showAlert(getContext(), "Update failed. Please try again !!", null)).addOnSuccessListener(taskSnapshot -> {
            info.setLogo(Constant.STORE + baseStore.getName());
            toUploadArrayImage(mDataImage);
        });
    }

    private void toUploadArrayImage(List<Bitmap> bitmap) {
        baseStore = FirebaseStorage.getInstance().getReferenceFromUrl(STORE + id + "_" + startImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.get(startImage).compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataImage = baos.toByteArray();
        UploadTask uploadTask = baseStore.putBytes(dataImage);
        uploadTask.addOnFailureListener(exception -> AppUtils.showAlert(getContext(), "Update failed. Please try again !!", null)).addOnSuccessListener(taskSnapshot -> {
            info.getDataImage().add(Constant.STORE + baseStore.getName());
            if (info.getDataImage().size() == bitmap.size()) {
                refHotel.child(id).setValue(info);
                mRoomModel = new RoomModel(tvMale.getText().toString(), tvFemale.getText().toString());
                refHotel_room.child(id).setValue(mRoomModel);
                refHotel_service.child(id).setValue(new Gson().toJson(mAddService));
                dismissLoading();
                AppUtils.showAlert(getActivity(), "Add hotel successful.", null);
                toResetData();
            } else
                EventBus.getDefault().post(new EventBusUpload(true));
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusUpload event) {
        startImage++;
        toUploadArrayImage(mDataImage);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    private void toLoadImage(String path) {
        baseStore = FirebaseStorage.getInstance().getReferenceFromUrl(path);
        baseStore.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(bytes -> {
            ivBanner.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }).addOnFailureListener(exception -> {
            ivBanner.setImageResource(R.drawable.ic_no_image);
        });
    }

    private void toLoadImageArray(List<String> mData) {
        for (String s : mData) {
            baseStore = FirebaseStorage.getInstance().getReferenceFromUrl(s);
            baseStore.getBytes(Constant.SIZE_DEFAULT).addOnSuccessListener(bytes -> {
                mDataImage.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                mAdapter.notifyDataSetChanged();
                if (mDataImage.size() == mData.size()) {
                    dismissLoading();
                }
            }).addOnFailureListener(exception -> {
            });
        }
    }


    private void toLoadData() {
        showLoading();
        toLoadImage(info.getLogo());
        tvName.setText(info.getName());
        tvPrice.setText(info.getPrice());
        tvAddress.setText(info.getAddress());
        tvLocation.setText(info.getLocation().getLat() + ", " + info.getLocation().getLng());
        tvDescription.setText(info.getDescription());
        tvMale.setText(mRoomModel.single);
        tvFemale.setText(mRoomModel._double);
        tvService.setText(AppUtils.toAddStringArray(mServiceDetailModel.getService()));
        toLoadImageArray(info.getDataImage());
    }

    private void toResetData() {
        ivBanner.setImageResource(R.drawable.ic_no_image);
        mDataImage.clear();
        mAdapter.notifyDataSetChanged();
        tvName.setText("");
        tvPrice.setText("");
        tvAddress.setText("");
        tvLocation.setText("");
        tvDescription.setText("");
        tvMale.setText("");
        tvService.setText("");
        tvFemale.setText("");
    }
}
