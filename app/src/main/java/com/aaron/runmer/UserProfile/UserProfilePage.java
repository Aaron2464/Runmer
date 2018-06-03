package com.aaron.runmer.UserProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aaron.runmer.DashBoardPackage.RunnerDashBoard;
import com.aaron.runmer.R;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfilePage extends Fragment implements UserProfileContract.View {

    ImageView mImageViewUserProfileAvatar;
    ImageButton mImageBtnUserProfileChange;
    TextView mTxtUserProfileLevel;
    TextView mTxtUserProfileName;
    TextView mTxtUserProfileCurrentExp;
    TextView mTxtUserProfileTotalExp;
    ProgressBar mBarUserProfileExp;
    RunnerDashBoard mDashBoardSpeedMax;
    RunnerDashBoard mDashBoardSpeedAvg;
    RunnerDashBoard mDashBoardWillPower;

    TextView mTxtAge, mTxtCal, mTxtEventCreated, mTxtEventJoined, mTxtSpeedAvg, mTxtDistaanceTotal, mTxtSpeedFast;
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
        mTxtUserProfileLevel = view.findViewById(R.id.txt_userprofile_userlevel);
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

        String userName = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_NAME, "");
        String userPhoto = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_PHOTO, "");
        String userBirth = getContext().getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).getString(Constants.USER_FIREBASE_BIRTH, "");
        int maxdistance = getContext().getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_DISTANCE, 0);
        int maxspeed = getContext().getSharedPreferences(Constants.USER_MAPPAGE_SPEED, MODE_PRIVATE).getInt(Constants.USER_MAPPAGE_MAXSPEED, 0);
        mHashMapUserStatus.put(Constants.USER_FIREBASE_NAME, userName);
        mHashMapUserStatus.put(Constants.USER_FIREBASE_PHOTO, userPhoto);
        mHashMapUserStatus.put(Constants.USER_FIREBASE_BIRTH, userBirth);

        mPresenter.setUserStatus(mHashMapUserStatus);
        mPresenter.setUserAge(mHashMapUserStatus);
        mPresenter.setUserExp(maxdistance);
        mPresenter.setUserMaxSpeed(maxspeed);
    }

    @Override
    public void showUserStatus(HashMap<String, String> mhashmap) {
        Picasso.get().load(mhashmap.get(Constants.USER_FIREBASE_PHOTO)).placeholder(R.drawable.running).transform(new CircleTransform(getContext())).into(mImageViewUserProfileAvatar);
        mTxtUserProfileName.setText(mhashmap.get(Constants.USER_FIREBASE_NAME));
    }

    @Override
    public void showUserBirth(int age) {
        mTxtAge.setText(String.valueOf(age));
    }

    @Override
    public void showUserExp(int maxdistance) {
        mBarUserProfileExp.setProgress(maxdistance);
        mTxtUserProfileCurrentExp.setText(String.valueOf(maxdistance));
        mTxtDistaanceTotal.setText(String.valueOf(maxdistance / 1000));
    }

    @Override
    public void showMaxSpeed(int maxSpeed) {
        mTxtSpeedFast.setText(String.valueOf(maxSpeed));
    }

    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
