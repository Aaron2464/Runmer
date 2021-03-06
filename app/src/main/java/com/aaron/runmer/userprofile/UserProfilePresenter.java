package com.aaron.runmer.userprofile;

import android.util.Log;

import com.aaron.runmer.api.RunmerParser;
import com.aaron.runmer.api.callback.CountEventsCreatedCallback;
import com.aaron.runmer.api.callback.CountEventsJoinedCallback;
import com.aaron.runmer.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfilePresenter implements UserProfileContract.Presenter {

    private UserProfileContract.View mUserProfileView;

    public UserProfilePresenter(UserProfileContract.View userProfileView) {
        mUserProfileView = checkNotNull(userProfileView);
        mUserProfileView.setPresenter(this);
    }

    @Override
    public void setUserStatus(HashMap<String, String> hashMapUserStatus) {
        mUserProfileView.showUserStatus(hashMapUserStatus);
    }

    @Override
    public void setUserAge(HashMap<String, String> hashMapUserStatus) {
        String userBirth = hashMapUserStatus.get(Constants.USER_FIREBASE_BIRTH);
        Date date = null;

        try {
            date = new SimpleDateFormat("yyyy").parse(userBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();//取得目前時間
        int currentyear = calendar.get(Calendar.YEAR);
//        Calendar c = Calendar.getInstance();
        calendar.setTime(date);
        int birthyear = calendar.get(Calendar.YEAR);
        int age = currentyear - birthyear;
        mUserProfileView.showUserBirth(age);
        Log.d(Constants.TAG, "Age: " + age);
        Log.d(Constants.TAG, "currentyear: " + currentyear);
        Log.d(Constants.TAG, "birthyear: " + birthyear);
    }

    @Override
    public void setUserExp(int maxdistance) {
        mUserProfileView.showUserExp(maxdistance);
    }

    @Override
    public void setUserMaxSpeed(int maxSpeed) {
        mUserProfileView.showMaxSpeed(maxSpeed);
    }

    @Override
    public void setUserAvgSpeed(int avgSpeed) {
        mUserProfileView.showAvgSpeed(avgSpeed);
    }

    @Override
    public void setEventsJoined() {
        RunmerParser.parseFirebaseEventsJoined(new CountEventsJoinedCallback() {
            @Override
            public void onCompleted(int countJoinedEvents) {
                mUserProfileView.showEventsJoined(countJoinedEvents);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setEventsCreated() {
        RunmerParser.parseFirebaseCountEventsCreated(new CountEventsCreatedCallback() {
            @Override
            public void onCompleted(int countEventsCreated) {
                mUserProfileView.showEventsCreated(countEventsCreated);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void setCalories(int distance, String userWeight) {
        double burnCalories = Integer.parseInt(userWeight) * distance / 1000 * 1.036;
        mUserProfileView.showCalories(burnCalories);
    }

    @Override
    public void start() {

    }
}
