package com.example.aaron.runmer.UserData;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPresenter implements UserDataContract.Presenter {

    private final UserDataContract.View mUserDataView;
    private FirebaseAuth mAuth;
    private String UserName;
    private String UserEmail;
    private String UserBirth;
    private String UserPhoto;

    public UserDataPresenter(UserDataContract.View userDataView) {
        mUserDataView = checkNotNull(userDataView);
        mUserDataView.setPresenter(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void setUserNameAndEmail() {
        UserName = mAuth.getCurrentUser().getDisplayName();
        UserEmail = mAuth.getCurrentUser().getEmail();
        mUserDataView.showUserNameAndEmail(UserName, UserEmail);
    }

    @Override
    public void setUserBirth() {
        mUserDataView.showUserBirth();
    }

    @Override
    public void setUserPhoto() {
        UserPhoto = mAuth.getCurrentUser().getPhotoUrl() + "?type=large";
        mUserDataView.showUserPhoto(UserPhoto);
    }

    @Override
    public void start() {

    }
}
