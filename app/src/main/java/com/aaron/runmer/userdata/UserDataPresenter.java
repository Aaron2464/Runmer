package com.aaron.runmer.userdata;

import android.net.Uri;

import com.aaron.runmer.api.RunmerParser;
import com.aaron.runmer.api.callback.PicReturnUriCallback;
import com.aaron.runmer.objects.UserData;
import com.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
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
    public void setUserNameAndEmail(String name, String email){
        mUserDataView.showUserNameAndEmail(name, email);
    }

    @Override
    public void setUserBirth(String birth){
        mUserDataView.showUserBirth(birth);
    }

    @Override
    public void setUserPhoto(String photo){
        mUserDataView.showUserPhoto(photo);
    }

    @Override
    public void setUserHeight(String height){
        mUserDataView.showUserHeight(height);
    }

    @Override
    public void setUserWeight(String weight){
        mUserDataView.showUserWeight(weight);
    }

    @Override
    public void setUserGender(String gender){
        mUserDataView.showUserGender(gender);
    }

    @Override
    public void setUserDataToFirebase(UserData userData) {
        String mUserUid = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("Users").child(mUserUid).setValue(userData);
        mDatabaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("UserUid").setValue(mAuth.getCurrentUser().getUid());
    }

    @Override
    public File createImageFile(File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    @Override
    public void uploadAndReturnUrl(Uri cameraUri) {
        RunmerParser.parseFirebaseStorage(cameraUri, new PicReturnUriCallback() {
            @Override
            public void onCompleted(Uri picReturnUri) {
                mUserPhoto = picReturnUri.toString();
                mUserDataView.showUserPhoto(mUserPhoto);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public String catchUserPhoto() {
        return mUserPhoto;
    }

    @Override
    public boolean isEmail(String userEmail) {
        if (userEmail == null) return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
    }


    @Override
    public void start() {
    }
}
