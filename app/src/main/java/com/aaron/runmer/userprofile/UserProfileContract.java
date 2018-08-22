package com.aaron.runmer.userprofile;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;

import java.util.HashMap;

public interface UserProfileContract {

    interface View extends BaseView<Presenter> {
        void showUserStatus(HashMap<String, String> mhashmap);

        void showUserBirth(int age);

        void showUserExp(int maxdistance);

        void showMaxSpeed(int maxSpeed);

        void showAvgSpeed(int avgSpeed);

        void showEventsJoined(int countEventsJoined);

        void showEventsCreated(int countEventsCreated);

        void showCalories(double burnCalories);
    }

    interface Presenter extends BasePresenter {

        void setUserStatus(HashMap<String, String> hashMapUserStatus);

        void setUserAge(HashMap<String, String> hashMapUserStatus);

        void setUserExp(int distance);

        void setUserMaxSpeed(int maxSpeed);

        void setUserAvgSpeed(int avgSpeed);

        void setEventsJoined();

        void setEventsCreated();

        void setCalories(int distance, String userWeight);
    }
}
