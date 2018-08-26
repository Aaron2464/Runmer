package com.aaron.runmer.userdata;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;
import com.aaron.runmer.objects.UserData;

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

        void changeUserImage();

        boolean isEmail(String userEmail);
    }
}
