package com.aaron.runmer.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aaron.runmer.R;
import com.aaron.runmer.dashboardpackage.RunnerDashBoard;
import com.aaron.runmer.userdata.UserDataPage;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfilePage extends Fragment implements UserProfileContract.View, View.OnClickListener {

    private ImageView mImageViewUserProfileAvatar;
    private ImageButton mImageBtnUserProfileChange;
    private TextView mTxtUserProfileLevel;
    private TextView mTxtUserProfileName;
    private TextView mTxtUserProfileCurrentExp;
    private TextView mTxtUserProfileTotalExp;
    private ProgressBar mBarUserProfileExp;
    private RunnerDashBoard mDashBoardSpeedMax;
    private RunnerDashBoard mDashBoardSpeedAvg;
    private RunnerDashBoard mDashBoardWillPower;

    private TextView mTxtAge;
    private TextView mTxtCal;
    private TextView mTxtEventCreated;
    private TextView mTxtEventJoined;
    private TextView mTxtSpeedAvg;
    private TextView mTxtDistaanceTotal;
    private TextView mTxtSpeedFast;
    HashMap<String, String> mHashMapUserStatus = new HashMap<>();
    private UserProfileContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserProfilePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageViewUserProfileAvatar = view.findViewById(R.id.imageview_userprofile_userimage);
        mImageBtnUserProfileChange = view.findViewById(R.id.imagebtn_userprofile_changeimage);
        mTxtUserProfileLevel = view.findViewById(R.id.txt_userprofile_userlevelnum);
        mTxtUserProfileName = view.findViewById(R.id.txt_userprofile_username);
        mTxtUserProfileCurrentExp = view.findViewById(R.id.txt_userprofile_currentexp);
        mTxtUserProfileTotalExp = view.findViewById(R.id.txt_userprofile_totalexp);
        mBarUserProfileExp = view.findViewById(R.id.progbar_userprofile_exp);
        mDashBoardSpeedMax = view.findViewById(R.id.dashboard_speed_fast);
        mDashBoardSpeedAvg = view.findViewById(R.id.dashboard_speed_avg);
        mDashBoardWillPower = view.findViewById(R.id.dashboard_willpower);

        mTxtAge = view.findViewById(R.id.txt_age);
        mTxtCal = view.findViewById(R.id.txt_calories);
        mTxtEventCreated = view.findViewById(R.id.txt_eventcreated);
        mTxtEventJoined = view.findViewById(R.id.txt_eventjoined);
        mTxtSpeedAvg = view.findViewById(R.id.txt_speedavg);
        mTxtDistaanceTotal = view.findViewById(R.id.txt_distancetotal);
        mTxtSpeedFast = view.findViewById(R.id.txt_speedfast);

        mImageBtnUserProfileChange.setOnClickListener(this);
        String userName = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_NAME, "");
        String userPhoto = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_PHOTO, "");
        String userBirth = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_BIRTH, "");
        mHashMapUserStatus.put(Constants.USER_FIREBASE_NAME, userName);
        mHashMapUserStatus.put(Constants.USER_FIREBASE_PHOTO, userPhoto);
        mHashMapUserStatus.put(Constants.USER_FIREBASE_BIRTH, userBirth);

        int distance = getContext().getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_DISTANCE, 0);
        int maxspeed = getContext().getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_MAXSPEED, 0);
        int avgspeed = getContext().getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_AVGSPEED, 0);
        String userWeight = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_WEIGHT, "");
        mPresenter.setUserStatus(mHashMapUserStatus);
        mPresenter.setUserAge(mHashMapUserStatus);
        mPresenter.setUserExp(distance);
        mPresenter.setUserMaxSpeed(maxspeed);
        mPresenter.setUserAvgSpeed(avgspeed);
        mPresenter.setEventsJoined();
        mPresenter.setEventsCreated();
        mPresenter.setCalories(distance, userWeight);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mImageViewUserProfileAvatar = getView().findViewById(R.id.imageUser_mapView);
//        String photo = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_PHOTO, "");
//        Log.d(Constants.TAG, "PHOTO: " + photo);
//        Picasso.get().load(photo).placeholder(R.drawable.user_image).transform(new CircleTransform(getContext())).into(mImageViewUserProfileAvatar);
//    }

    @Override
    public void showUserStatus(HashMap<String, String> mhashmap) {
        Picasso.get().load(mhashmap.get(Constants.USER_FIREBASE_PHOTO)).placeholder(R.drawable.runningicon).transform(new CircleTransform(getContext())).into(mImageViewUserProfileAvatar);
        mTxtUserProfileName.setText(mhashmap.get(Constants.USER_FIREBASE_NAME));
    }

    @Override
    public void showUserBirth(int age) {
        mTxtAge.setText(String.valueOf(age));
    }

    @Override
    public void showUserExp(int maxdistance) {
        if (maxdistance < 10000) {
            mBarUserProfileExp.setProgress(maxdistance);
            mTxtUserProfileCurrentExp.setText(String.valueOf(maxdistance));
        } else if (maxdistance < 25000) {
            mTxtUserProfileLevel.setText(String.valueOf(2));
            mBarUserProfileExp.setMax(15000);
            mBarUserProfileExp.setProgress(maxdistance - 10000);
            mTxtUserProfileCurrentExp.setText(String.valueOf(maxdistance));
            mTxtUserProfileTotalExp.setText(String.valueOf(25000));
        } else if (maxdistance < 45000) {
            mTxtUserProfileLevel.setText(String.valueOf(3));
            mBarUserProfileExp.setMax(20000);
            mBarUserProfileExp.setProgress(maxdistance - 25000);
            mTxtUserProfileCurrentExp.setText(String.valueOf(maxdistance));
            mTxtUserProfileTotalExp.setText(String.valueOf(45000));
        } else {
            mTxtUserProfileLevel.setText(String.valueOf(4));
            mBarUserProfileExp.setMax(35000);
            mBarUserProfileExp.setProgress(maxdistance - 45000);
            mTxtUserProfileCurrentExp.setText(String.valueOf(maxdistance));
            mTxtUserProfileTotalExp.setText(String.valueOf(80000));
        }
        mTxtDistaanceTotal.setText(String.valueOf(maxdistance / 1000));
    }

    @Override
    public void showMaxSpeed(int maxSpeed) {
        mDashBoardSpeedMax.setVelocity(maxSpeed);
        mTxtSpeedFast.setText(String.valueOf(maxSpeed));
    }

    @Override
    public void showAvgSpeed(int avgSpeed) {
        mDashBoardSpeedAvg.setVelocity(avgSpeed);
        mTxtSpeedAvg.setText(String.valueOf(avgSpeed));
    }

    @Override
    public void showEventsJoined(int countEventsJoined) {
        mTxtEventJoined.setText(String.valueOf(countEventsJoined));
        Log.d(Constants.TAG, "eventCount" + countEventsJoined);
    }

    @Override
    public void showEventsCreated(int countEventsCreated) {
        mTxtEventCreated.setText(String.valueOf(countEventsCreated));
    }

    @Override
    public void showCalories(double burnCalories) {
        if ((int) burnCalories <= 9999.99) {
            DecimalFormat df = new DecimalFormat("##.00");
            mTxtCal.setText(df.format(burnCalories));
        } else if (99999 >= (int) burnCalories && (int) burnCalories > 9999.99) {
            DecimalFormat df = new DecimalFormat("##.0");
            mTxtCal.setText(df.format(burnCalories));
        } else {
            mTxtCal.setText(String.valueOf((int) burnCalories));
        }
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagebtn_userprofile_changeimage:
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setClass(getContext(), UserDataPage.class);
                startActivity(intent);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
