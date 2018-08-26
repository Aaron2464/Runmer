package com.aaron.runmer.map;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.runmer.R;
import com.aaron.runmer.api.GpsServices;
import com.aaron.runmer.base.BaseActivity;
import com.aaron.runmer.dashboardpackage.RunnerDashBoard;
import com.aaron.runmer.userdata.UserDataPage;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.aaron.runmer.viewpagermain.ViewPagerActivity;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPage extends BaseActivity implements MapContract.View,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        android.location.LocationListener,
        com.google.android.gms.location.LocationListener {

    private MapContract.Presenter mPresenter;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private TextToSpeech textToSpeech;
    private Marker mMarker;
    private Map<String, Marker> mMarkerMap;
    private Map<String, String> mUriMap;
    private RunnerDashBoard mRunnerDashBoardIntimeSpeed;
    private RunnerDashBoard mRunnerDashBoardAvgSpeed;
    private Switch mSwitch;
    private ProgressBar mBarExp;
    private EditText mEditTxtCommentMessage;
    private TextView mTxtCommentRight;
    private TextView mTxtCommentLeft;
    private TextView mTxtExpCurrent;
    private TextView mTxtExpTotal;
    private ImageView mImageUser;
    private ImageButton mBtnSendComment;
    private ConstraintLayout mMapPageConstraintLayout;
    private SpeedData.OnGpsServiceUpdate onGpsServiceUpdate;
    private static SpeedData data;
    private boolean isAnimFinished = true;
    private SharedPreferences sharedPreferences;
    private Long mStartTime;
    private Long mCurrentTime;
    private Long mTime;
    private String bestProv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        mMarkerMap = new HashMap<String, Marker>();
        mUriMap = new HashMap<String, String>();
        mPresenter = new MapPresenter(this);
        mStartTime = SystemClock.elapsedRealtime();
        init();
        mPresenter.setUserPhoto();
        startGps();
    }

    private void startGps() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        data.setRunning(true);
        data.setFirstTime(true);
        startService(new Intent(getBaseContext(), GpsServices.class));
        onGpsServiceUpdate = new SpeedData.OnGpsServiceUpdate() {
            @Override
            public void update() {
                double maxSpeedTemp = data.getMaxSpeed();
                double distanceTemp = data.getDistance();
                double averageTemp;
                if (sharedPreferences.getBoolean("auto_average", false)) {
                    averageTemp = data.getAverageSpeedMotion();
                } else {
                    averageTemp = data.getAverageSpeed();
                }

                if (sharedPreferences.getBoolean("miles_per_hour", false)) {
                    maxSpeedTemp *= 0.62137119;
                    distanceTemp = distanceTemp / 1000.0 * 0.62137119;
                    averageTemp *= 0.62137119;
                }
                getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).edit()
                        .putInt(Constants.USER_MAPPAGE_MAXSPEED, (int) maxSpeedTemp)
                        .putInt(Constants.USER_MAPPAGE_DISTANCE, (int) distanceTemp)
                        .putInt(Constants.USER_MAPPAGE_AVGSPEED, (int) averageTemp).apply();
                mRunnerDashBoardAvgSpeed.setVelocity((int) averageTemp);
                mPresenter.setUserExp((int) distanceTemp);
                Log.d(Constants.TAG, "distanceTemp: " + String.valueOf(distanceTemp));
                Log.d(Constants.TAG, "averageTemp: " + String.valueOf(averageTemp));
                Log.d(Constants.TAG, "maxSpeedTemp: " + String.valueOf(maxSpeedTemp));
            }
        };
    }

    public static SpeedData getData() {
        return data;
    }

    private void init() {
        mBarExp = findViewById(R.id.progressBar_exp);
        mTxtCommentLeft = findViewById(R.id.txt_leftcomment);
        mTxtCommentRight = findViewById(R.id.txt_rightcomment);
        mTxtExpCurrent = findViewById(R.id.txt_mappage_currentexp);
        mTxtExpTotal = findViewById(R.id.txt_mappage_totaleexp);
        mMapPageConstraintLayout = findViewById(R.id.map_page_layout);
        mRunnerDashBoardIntimeSpeed = findViewById(R.id.dashboard_speed);
        mRunnerDashBoardAvgSpeed = findViewById(R.id.dashboard_avg);
        mBtnSendComment = findViewById(R.id.CommentOption_btn_fb_send_comment);
        mEditTxtCommentMessage = findViewById(R.id.CommentOption_edittxt_comments_message);
        data = new SpeedData(onGpsServiceUpdate);
        int distance = mContext.getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_DISTANCE, 0);
        int maxSpeed = mContext.getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_MAXSPEED, 0);
        Log.d(Constants.TAG, "MaxDistance: " + distance);
        data.addDistance((double) distance);
        data.setCurSpeed(maxSpeed);
        setupMyLocation();
        selectUserStatus();
        sendGeoMessageToFriend();
        mPresenter.setUserExp(distance);
    }

    @Override
    public void showUserExp(int userExp, int nextExp, int barLength, int barLengthMax) {
        mTxtExpTotal.setText(String.valueOf(nextExp));
        mTxtExpCurrent.setText(String.valueOf(userExp));
        mBarExp.setMax(barLengthMax);
        mBarExp.setProgress(barLength);
    }

    private void sendGeoMessageToFriend() {
        mBtnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cheermessage = mEditTxtCommentMessage.getText().toString();
                if (!cheermessage.equals("")) {
                    mPresenter.sendMessage(cheermessage);
                } else {
                    mPresenter.noMessage();
                }
            }
        });
    }

    private void dashBoardAnimation(final RunnerDashBoard runnerDashBoard, int speed) {

        if (isAnimFinished) {
            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofInt(runnerDashBoard, "mRealTimeValue",
                    runnerDashBoard.getVelocity(), speed);
            animator.setDuration(1500).setInterpolator(new LinearInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimFinished = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimFinished = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimFinished = true;
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    runnerDashBoard.setVelocity(value);
                }
            });
            animator.start();
        }
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
                    Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, Constants.REQUEST_LOCATION).show();
            } else {
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
        Criteria criteria = new Criteria();
        bestProv = mLocationManager.getBestProvider(criteria, true);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Constants.DISPLACEMENT);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mPresenter.openGoogleMaps(mLocation);
    }

    /**
     * show google map, add marker, create a circle
     *
     * @param lat
     * @param lng
     */
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

    /**
     * 讓方圓300公尺的朋友能顯示在地圖上，點擊marker時可以看到好友頭像
     *
     * @param key
     * @param mlocation
     * @param friendAvatar
     */
    @Override
    public void showGeoFriends(String key, GeoLocation mlocation, String friendAvatar) {

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(mlocation.latitude, mlocation.longitude)));
        Log.d(Constants.TAG, "MMarker: " + marker.getId());
        mMarkerMap.put(key, marker);
        Log.d(Constants.TAG, "MKEY: " + key);
        Log.d(Constants.TAG, "MUri: " + friendAvatar.toString());
        mUriMap.put(marker.getId(), friendAvatar);
        mMap.setInfoWindowAdapter(new UserinfoWindow(mContext, mUriMap));
    }

    /**
     * when friend leave the geofire, remove the key and marker
     *
     * @param key
     */
    @Override
    public void removeGeoFriends(String key) {
        Marker marker = mMarkerMap.get(key);
        if (marker != null) {
            marker.remove();
            mMarkerMap.remove(key);
            mMarkerMap.clear();            //目前還不知道有什麼影響，特此註解以供驗證
        }
    }

    /**
     * move the marker, when friend move
     *
     * @param key
     * @param location
     */
    @Override
    public void moveGeoFriends(String key, GeoLocation location) {
        final double lat = location.latitude;
        final double lng = location.longitude;
        final Marker marker = mMarkerMap.get(key);
        if (marker != null) {               //            this.animateMarkerTo(marker, location.latitude, location.longitude)↓
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long durationMs = 3000;
            final Interpolator interpolator = new AccelerateDecelerateInterpolator();
            final LatLng startPosition = marker.getPosition();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    float elapsed = SystemClock.uptimeMillis() - start;
                    float t = elapsed / durationMs;
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

    /**
     * talk to friend, show left comment
     *
     * @param message
     */
    @Override
    public void showLeftComment(String message) {
        mTxtCommentLeft.setVisibility(View.VISIBLE);
        mTxtCommentRight.setVisibility(View.GONE);
        mTxtCommentLeft.setText(message);
        mEditTxtCommentMessage.setText("");
    }

    /**
     * friend's comment when friend send comment
     *
     * @param message
     */
    @Override
    public void showRightComment(final String message) {
        mTxtCommentRight.setVisibility(View.VISIBLE);
        mTxtCommentLeft.setVisibility(View.GONE);
        mTxtCommentRight.setText(message);

        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.TAIWAN);
                    textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    /**
     * show toast  no comment, when  click send btn
     */
    @Override
    public void noComment() {
        Snackbar.make(mMapPageConstraintLayout, "可以講講話啊", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * show avatar, when click the friend marker
     *
     * @param userimage
     */
    @Override
    public void showUserPhoto(String userimage) {
        mImageUser = findViewById(R.id.imageUser_mapView);
        Log.d(Constants.TAG, "MapPageUserImage :" + userimage);
        Picasso.get().load(userimage).placeholder(R.drawable.user_image).transform(new CircleTransform(mContext)).into(mImageUser);
        clickUserImage();
    }

    /**
     * Enter the detail page
     */
    private void clickUserImage() {
        mImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mContext.getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_NAME, "");
                if (userName.equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, UserDataPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(MapPage.this, ViewPagerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleApiClient.connect();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        displayLocation();
        mPresenter.queryfriendlocation(location);
        calculateSpeed(location);
    }

    private void calculateSpeed(Location location) {
        if (location.hasSpeed()) {
            int speed = (int) (location.getSpeed() * 3.6);
            dashBoardAnimation(mRunnerDashBoardIntimeSpeed, speed);
            Log.d(Constants.TAG_DASHBOARD, "Speed: " + speed);
            mCurrentTime = SystemClock.elapsedRealtime();
            mTime = mCurrentTime - mStartTime;
            Log.e(Constants.TAG, "mCurrentTime:" + mCurrentTime);
            Log.e(Constants.TAG, "mStartTime:" + mStartTime);
            data.setTime(mTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.setRunning(false);
        stopService(new Intent(getBaseContext(), GpsServices.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data == null) {
            data = new SpeedData(onGpsServiceUpdate);
        } else {
            data.setOnGpsServiceUpdate(onGpsServiceUpdate);
        }

        if (!data.isRunning()) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("data", "");
            data = gson.fromJson(json, SpeedData.class);
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(bestProv, 3000, 0, this);
            }
        } else {
            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDisabledDialog();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
//        mLocationManager.addGpsStatusListener((GpsStatus.Listener) this);
    }

    public void showGpsDisabledDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder
                .setCancelable(false)
                .setMessage("Please turn the GPS!")
                .setPositiveButton("check", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
//        mLocationManager.removeGpsStatusListener((GpsStatus.Listener) this);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("data", json);
        prefsEditor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        createLocationRequest();
                        buildGoogleApiClient();
                        displayLocation();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}