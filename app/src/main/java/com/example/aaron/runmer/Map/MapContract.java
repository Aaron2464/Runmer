package com.example.aaron.runmer.Map;

import android.location.Location;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        void showGoogleMapUi(double lat, double lng);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openGoogleMaps(Location mLocation);

        void setUserStatus(boolean isChecked);
    }
}
