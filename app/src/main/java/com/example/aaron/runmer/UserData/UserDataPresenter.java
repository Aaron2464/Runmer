package com.example.aaron.runmer.UserData;

import android.net.Uri;

import com.example.aaron.runmer.UserProfile.UserProfileContract;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPresenter implements UserDataContract.Presenter{

    private final UserDataContract.View mUserDataView;
    private FirebaseAuth mAuth;
    private String UserName;
    private String UserEmail;
    private String UserBirth;
    private Uri UserPhoto;

    public UserDataPresenter(UserDataContract.View userDataView) {
        mUserDataView = checkNotNull(userDataView);
        mUserDataView.setPresenter(this);
    }

    @Override
    public void setUserNameAndEmail() {
        mUserDataView.showUserNameAndEmail();
    }


    @Override
    public void setUserBirth() {

    }

    @Override
    public void setUserPhoto() {

    }

    @Override
    public void start() {

    }

}
