package com.example.aaron.runmer.UserData;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;

public interface UserDataContract {

    interface View extends BaseView<UserDataContract.Presenter> {

        void showUserNameAndEmail();

        void showUserBirth();

        void showUserPhoto();
    }

    interface Presenter extends BasePresenter {

        void setUserNameAndEmail();

        void setUserBirth();

        void setUserPhoto();

//        void getUserData();
    }
}
