package com.example.aaron.runmer.UserData;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;

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

//        void getUserData();
    }
}
