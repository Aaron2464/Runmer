package com.aaron.runmer.userdata;

import com.aaron.runmer.objects.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPresenter implements UserDataContract.Presenter {

    private final UserDataContract.View mUserDataView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private String mUserName;
    private String mUserEmail;
    private String mUserPhoto;

    public UserDataPresenter(UserDataContract.View userDataView) {
        mUserDataView = checkNotNull(userDataView);
        mUserDataView.setPresenter(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void setUserNameAndEmail() {
        mUserName = mAuth.getCurrentUser().getDisplayName();
        mUserEmail = mAuth.getCurrentUser().getEmail();
        mUserDataView.showUserNameAndEmail(mUserName, mUserEmail);
    }

    @Override
    public void setUserBirth() {
        mUserDataView.showUserBirth();
    }

    @Override
    public void setUserPhoto() {
        mUserPhoto = mAuth.getCurrentUser().getPhotoUrl() + "?type=large";
        mUserDataView.showUserPhoto(mUserPhoto);
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
    public boolean isEmail(String mUserEmail) {
        if (mUserEmail == null) return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mUserEmail).matches();
    }


    @Override
    public void start() {
    }
}
