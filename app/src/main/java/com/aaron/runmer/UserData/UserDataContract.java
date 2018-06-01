package com.aaron.runmer.UserData;

import com.aaron.runmer.Base.BasePresenter;
import com.aaron.runmer.Base.BaseView;

import java.util.Map;

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

        void setUserDataToFirebase(Map UserDataMap);

        void changeUserImage();
    }
}
