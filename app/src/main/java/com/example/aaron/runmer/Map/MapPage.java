package com.example.aaron.runmer.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.aaron.runmer.Base.BaseActivity;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.ViewPagerMain.ViewPagerActivity;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.example.aaron.runmer.util.Constants.DISPLACEMENT;
import static com.example.aaron.runmer.util.Constants.FATEST_INTERVAL;
import static com.example.aaron.runmer.util.Constants.REQUEST_LOCATION;
import static com.example.aaron.runmer.util.Constants.UPDATE_INTERVAL;
import static com.google.common.base.Preconditions.checkNotNull;

public class MapPage extends BaseActivity implements MapContract.View
        , GoogleMap.OnMyLocationButtonClickListener
        , OnMapReadyCallback, LocationListener
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    private MapContract.Presenter mPresenter;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private Marker mMarker;
    private Switch mSwitch;
    private ImageView mImageUser;
    private Map<String, Marker> mMarkerMap;
    private Map<String, String> mUriMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        this.mMarkerMap = new HashMap<String, Marker>();
        this.mUriMap = new HashMap<String, String>();
        mPresenter = new MapPresenter(this);
        mPresenter.setUserPhoto();
        setupMyLocation();
        selectUserStatus();
    }

    private void selectUserStatus() {
        mSwitch = findViewById(R.id.switch_status);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startLocationUpdate();
                    mPresenter.setUserStatus(isChecked);
                    Log.d(Constants.TAG, "User Statua: " + isChecked);
//                    Toast.makeText(mContext,"x8 x8 x8 x8 ",Toast.LENGTH_LONG).show();
                } else {
                    stopLocationUpdate();
                    mPresenter.setUserStatus(isChecked);
                    Log.d(Constants.TAG, "User Statua: " + isChecked);
                }
            }
        });
    }

    private void setupMyLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            if (checkPlayServices()) {                      //mMapPageModel.checkPlayServices();
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, REQUEST_LOCATION).show();
            else {
                Toast.makeText(this, "不支援此裝置", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mPresenter.openGoogleMaps(mLocation);
    }

    @SuppressLint("MissingPermission")
    public void showGoogleMapUi(double lat, double lng) {
        LatLng userlocation = new LatLng(lat, lng);
        Log.d(Constants.TAG, "CurrentLocation: " + userlocation);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 18));
        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setMyLocationEnabled(true);        //show my location blue dot
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(userlocation)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_flight)));
        mMarker.getRotation();
        mMap.addCircle(new CircleOptions()
                .center(userlocation)
                .radius(30)
                .strokeColor(Color.BLACK)
                .strokeWidth(10.0f));

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 13));
//        mMap.getUiSettings().setZoomControlsEnabled(true);\
    }

    @Override
    public void showGeoFriends(String key, GeoLocation mlocation, String friendAvatar) {
        mMarkerMap.clear();                   //目前還不知道有什麼影響，特此註解以供驗證
        mUriMap.clear();                       //目前還不知道有什麼影響，特此註解以供驗證
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(mlocation.latitude, mlocation.longitude)));
        Log.d(Constants.TAG, "MMarker: " + marker.getId());
        this.mMarkerMap.put(key, marker);
        Log.d(Constants.TAG, "MKEY: " + key);
        Log.d(Constants.TAG, "MUri: " + friendAvatar.toString());
        this.mUriMap.put(marker.getId(),friendAvatar);
        mMap.setInfoWindowAdapter(new UserinfoWindow(this,mUriMap));
    }

    @Override
    public void removeGeoFriends(String key) {
        Marker marker = this.mMarkerMap.get(key);
        if (marker != null) {
            marker.remove();
            this.mMarkerMap.remove(key);
            this.mMarkerMap.clear();            //目前還不知道有什麼影響，特此註解以供驗證
        }
    }

    @Override
    public void moveGeoFriends(String key, GeoLocation location){
        final double lat = location.latitude;
        final double lng = location.longitude;
        final Marker marker = this.mMarkerMap.get(key);
        if (marker != null) {               //            this.animateMarkerTo(marker, location.latitude, location.longitude)↓
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long DURATION_MS = 3000;
            final Interpolator interpolator = new AccelerateDecelerateInterpolator();
            final LatLng startPosition = marker.getPosition();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    float elapsed = SystemClock.uptimeMillis() - start;
                    float t = elapsed/DURATION_MS;
                    float v = interpolator.getInterpolation(t);

                    double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                    double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                    marker.setPosition(new LatLng(currentLat, currentLng));

                    // if animation is not finished yet, repeat
                    if (t < 1) {
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
    }

    @Override
    public void showUserPhoto(String userimage) {
        mImageUser = findViewById(R.id.imageUser_mapView);
        Log.d(Constants.TAG, "MapPageUserImage :" + userimage);
        Picasso.get().load(userimage).placeholder(R.drawable.user_image).transform(new CircleTransform(mContext)).into(mImageUser);
        clickUserImage();
    }

    private void clickUserImage() {
        mImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapPage.this, ViewPagerActivity.class);
                startActivity(intent);
//                MapPage.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        displayLocation();
        mPresenter.queryfriendlocation(location);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        createLocationRequest();
                        buildGoogleApiClient();
                        displayLocation();
                    }
                }
                break;
        }
    }
}