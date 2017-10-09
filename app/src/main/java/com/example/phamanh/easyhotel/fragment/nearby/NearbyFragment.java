package com.example.phamanh.easyhotel.fragment.nearby;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.base.BaseModel;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.model.EventBusLocationModel;
import com.example.phamanh.easyhotel.model.NearPlacesModel;
import com.example.phamanh.easyhotel.network.GoogleApi;
import com.example.phamanh.easyhotel.other.database.DataHardCode;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.phamanh.easyhotel.utils.Constant.GOOGLE_API_URL;


public class NearbyFragment extends BaseFragment implements OnMapReadyCallback, DirectionCallback, GoogleMap.OnInfoWindowClickListener {

    private final int PROXIMITY_RADIUS = 1000;

    @BindView(R.id.fragMap_llSearch)
    LinearLayout searchPlace;
    @BindView(R.id.fragmentMap_drawer)
    DrawerLayout mapDrawer;
    @BindView(R.id.fragOther_tvPlace)
    TextView tvPlace;
    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;

    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private Marker mCurrLocationMarker;
    private double latitude, longitude, lat, lng;
    private Polyline polyline;
    private GoogleMap googleMap;
    private LatLng mLatLngForm;
    private String json = "";
    private boolean isCheckLocation;
    private LatLngBounds mBound;
    private List<String> mDataNearby = new ArrayList<>();
    private SelectSinglePopup popupNearby;
    private ArrayList<Marker> mArrayMarker = new ArrayList<>();
    private ArrayList<NearPlacesModel.Result> mArrayResultsMarker = new ArrayList<>();
    private View view;

    Unbinder unbinder;

    public static NearbyFragment newInstance(BaseModel model) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.BASE_MODEL, model);
        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_nearby, container, false);
        unbinder = ButterKnife.bind(this, view);
        setActionBar(view, getString(R.string.page_other));
        KeyboardUtils.setupUI(view, getActivity());
        ivBack.setVisibility(View.GONE);
        return view;
    }

    private void init() {
        if (mDataNearby.size() == 0)
            mDataNearby.addAll(DataHardCode.getListNearby());
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap_map));
        if (mapFragment.isHidden())
            getFragmentManager().beginTransaction().show(mapFragment).commit();
        toSetUpMap();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusLocationModel data) {
        if (data != null) {
            lat = data.lat;
            lng = data.lng;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onPause() {
        if (mapFragment.isVisible())
            getFragmentManager().beginTransaction().hide(mapFragment).commit();
        super.onPause();
    }

    private void toSetUpMap() {
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationUpdate();
    }

    private void showPopupRelationship() {
        if (popupNearby == null) {
            popupNearby = new SelectSinglePopup(getActivity(), mDataNearby, false);
        }
        popupNearby.setOnItemListener(pos -> {
            tvPlace.setText(mDataNearby.get(pos));
            tvPlace.setTextColor(ContextCompat.getColor(getActivity(), R.color.denimBlue));
            loadMarker(toGetPlace(pos));
            popupNearby.dismiss();
            showLoading();
        });
        popupNearby.showAsDropDown(searchPlace);
    }

    private String toGetPlace(int index) {
        String s = "";
        switch (index) {
            case 0:
                s = "movie_theater";
                break;
            case 1:
                s = "cafe";
                break;
            case 2:
                s = "hospital";
                break;
            case 3:
                s = "restaurant";
                break;
            case 4:
                s = "police";
                break;
            case 5:
                s = "atm";
                break;
            case 6:
                s = "bank";
                break;
        }
        return s;
    }

    private void loadZoomCamera() {
        if (mArrayMarker.size() != 0) {
            LatLngBounds.Builder fFBuilder = new LatLngBounds.Builder();
            for (Marker m : mArrayMarker) {
                fFBuilder.include(m.getPosition());
            }
            mBound = fFBuilder.build();
            googleMap.setOnMapLoadedCallback(() -> {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBound, 100));//padding
            });
        }
    }

    GoogleMap.OnMyLocationChangeListener onMyLocation = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLatLngForm = new LatLng(location.getLatitude(), location.getLongitude());
            if (googleMap != null && !isCheckLocation) {
                showLoading();
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngForm, 17));
                        isCheckLocation = true;
                        latitude = mLatLngForm.latitude;
                        longitude = mLatLngForm.longitude;
                        dismissLoading();
                    }
                }.start();
            }
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mCurrLocationMarker != null)
                mCurrLocationMarker.remove();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            AppUtils.showAlert(getContext(), "Please enable location !", null);
        }
    };

    private void setupLocationMarkerButton() {
        View mapView = mapFragment.getView();
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }

    private GoogleApi connectGoogleApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(GoogleApi.class);
    }

    private void loadMarker(String type) {
        Call<NearPlacesModel> call = connectGoogleApi().getNearbyPlaces(type, latitude + "," + longitude, PROXIMITY_RADIUS);
        call.enqueue(new Callback<NearPlacesModel>() {
            @Override
            public void onResponse(Call<NearPlacesModel> call, Response<NearPlacesModel> response) {
                try {
                    googleMap.clear();
                    if (mArrayResultsMarker.size() != 0)
                        mArrayResultsMarker.clear();
                    mArrayResultsMarker.addAll(response.body().getResults());
                    if (mArrayResultsMarker.size() != 0) {
                        for (int i = 0; i < mArrayResultsMarker.size(); i++) {
                            drawMarkerIcon(mArrayResultsMarker.get(i));
                        }
                    } else
                        Toast.makeText(getActivity(), "Not found " + tvPlace.getText().toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Load data failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesModel> call, Throwable t) {

            }
        });
    }

    private void drawMarkerIcon(NearPlacesModel.Result item) {
        Double lat = item.getGeometry().getLocation().getLat();
        Double lng = item.getGeometry().getLocation().getLng();
        String placeName = item.getName();
        String vicinity = item.getVicinity();
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(lat, lng);
        markerOptions.position(latLng);
        markerOptions.title(placeName + " : " + vicinity);
        LoadIcon loadIcon = new LoadIcon(markerOptions);
        loadIcon.execute(item.getIcon());
    }

    private class LoadIcon extends AsyncTask<String, Void, Bitmap> {
        private MarkerOptions options;

        LoadIcon(MarkerOptions option) {
            this.options = option;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return AppUtils.getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                mArrayMarker.add(googleMap.addMarker(options));
                if (mArrayMarker.size() == mArrayResultsMarker.size()) {
                    loadZoomCamera();
                    mArrayMarker.clear();
                    dismissLoading();
                }
            }
        }
    }

    private void locationUpdate() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
        }
    }

    private void getBundle() throws JSONException {
        Bundle bundle = getArguments();
        if (bundle != null) {
            json = bundle.getString("pos_json");
            drawPolylineTracking();
        }
    }

    private void drawPolylineTracking() throws JSONException {
        JSONArray array = new JSONArray(json);
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject location = array.getJSONObject(i);
            double lat = Double.parseDouble(location.optString("latitude"));
            double lng = Double.parseDouble(location.optString("longitude"));
            points.add(i, new LatLng(lat, lng));
        }
        for (int i = 0; i < points.size() - 1; i++) {
            LatLng src = points.get(i);
            LatLng dest = points.get(i + 1);
            // mMap is the Map Object
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude), 15));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (googleMap == null)
            googleMap = map;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            setupLocationMarkerButton();
            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setOnMyLocationChangeListener(onMyLocation);
            if (lat != 0 && lng != 0) {
                LatLng currentLatLng1 = new LatLng(lat, lng);
                if (!currentLatLng1.equals(googleMap.addMarker(new MarkerOptions().position(currentLatLng1)))) {
                    GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                            .from(new LatLng(lat, lng))
                            .to(googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))).getPosition())
                            .avoid(AvoidType.FERRIES)
                            .avoid(AvoidType.HIGHWAYS)
                            .execute(NearbyFragment.this);
                }
            }
        } else requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        try {
            getBundle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.fragMap_llSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragMap_llSearch:
                showPopupRelationship();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            if (polyline != null) {
                polyline.remove();
            }
            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);
            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED);
            polyline = googleMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        AppUtils.showAlertMap(getContext(), marker.getTitle(), new DialogListener() {
            @Override
            public void onConfirmClicked() {
                LatLng currentLatLng1 = new LatLng(latitude, longitude);
                if (!currentLatLng1.equals(marker.getPosition())) {
                    GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                            .from(new LatLng(latitude, longitude))
                            .to(marker.getPosition())
                            .avoid(AvoidType.FERRIES)
                            .avoid(AvoidType.HIGHWAYS)
                            .execute(NearbyFragment.this);
                }
            }

            @Override
            public void onCancelClicked() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
