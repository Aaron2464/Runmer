package com.aaron.runmer.UserProfile;

import com.aaron.runmer.Base.BasePresenter;
import com.aaron.runmer.Base.BaseView;

import java.util.HashMap;

public interface UserProfileContract {

    interface View extends BaseView<Presenter> {
        void showUserStatus(HashMap<String, String> mhashmap);

        void showUserBirth(int age);
    }

    interface Presenter extends BasePresenter {

        void setUserStatus(HashMap<String, String> hashMapUserStatus);

        void setUserAge(HashMap<String, String> hashMapUserStatus);
    }
}
