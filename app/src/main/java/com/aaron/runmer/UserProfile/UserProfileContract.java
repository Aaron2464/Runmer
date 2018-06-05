package com.aaron.runmer.UserProfile;

import com.aaron.runmer.Base.BasePresenter;
import com.aaron.runmer.Base.BaseView;

import java.util.HashMap;

public interface UserProfileContract {

    interface View extends BaseView<Presenter> {
        void showUserStatus(HashMap<String, String> mhashmap);

        void showUserBirth(int age);

        void showUserExp(int maxdistance);

        void showMaxSpeed(int maxSpeed);

        void showAvgSpeed(int avgSpeed);

        void showEventsJoined(int CountEventsJoined);

        void showEventsCreated(int CountEventsCreated);
    }

    interface Presenter extends BasePresenter {

        void setUserStatus(HashMap<String, String> hashMapUserStatus);

        void setUserAge(HashMap<String, String> hashMapUserStatus);

        void setUserExp(int maxdistance);

        void setUserMaxSpeed(int maxSpeed);

        void setUserAvgSpeed(int avgSpeed);

        void setEventsJoined();

        void setEventsCreated();

    }
}
