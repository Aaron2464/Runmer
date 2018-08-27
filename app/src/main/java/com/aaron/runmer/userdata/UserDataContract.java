package com.aaron.runmer.userdata;

import android.net.Uri;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;
import com.aaron.runmer.objects.UserData;

import java.io.File;
import java.io.IOException;

public interface UserDataContract {

    interface View extends BaseView<UserDataContract.Presenter> {

        void showUserNameAndEmail(String userName, String userEmail);

        void showUserBirth();

        void showUserPhoto(String userimage);
    }

    interface Presenter extends BasePresenter {

        void setUserNameAndEmail();

        void setUserBirth();

        void setUserPhoto();

        void setUserDataToFirebase(UserData userdata);

        File createImageFile(File storageDir) throws IOException;

        void uploadAndReturnUrl(Uri cameraUri);

        String catchUserPhoto();

        boolean isEmail(String userEmail);
    }
}
