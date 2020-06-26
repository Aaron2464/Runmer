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

        void showUserBirth(String birth);

        void showUserPhoto(String userImage);

        void showUserHeight(String height);

        void showUserWeight(String weight);

        void showUserGender(String gender);
    }

    interface Presenter extends BasePresenter {

        void setUserNameAndEmail();

        void setUserBirth();

        void setUserPhoto();

        void setUserNameAndEmail(String name, String email);

        void setUserBirth(String birth);

        void setUserPhoto(String photo);

        void setUserHeight(String height);

        void setUserWeight(String weight);

        void setUserGender(String gender);

        void setUserDataToFirebase(UserData userdata);

        File createImageFile(File storageDir) throws IOException;

        void uploadAndReturnUrl(Uri cameraUri);

        String catchUserPhoto();

        boolean isEmail(String userEmail);
    }
}
