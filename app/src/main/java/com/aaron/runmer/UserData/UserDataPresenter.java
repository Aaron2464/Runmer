package com.aaron.runmer.UserData;

import com.aaron.runmer.Objects.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPresenter implements UserDataContract.Presenter {

    private final UserDataContract.View mUserDataView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
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
    public void setUserDataToFirebase(UserData userData) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userData);
        mDatabaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("UserUid").setValue(mAuth.getCurrentUser().getUid());
    }

    @Override
    public void changeUserImage() {

    }


    @Override
    public void start() {
    }
}
