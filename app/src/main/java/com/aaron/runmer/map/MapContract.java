package com.aaron.runmer.map;

import android.location.Location;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;
import com.firebase.geofire.GeoLocation;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        void showGoogleMapUi(double lat, double lng);

        void showUserPhoto(String userimage);

        void showGeoFriends(String key, GeoLocation location, String friendAvatar);

        void moveGeoFriends(String key, GeoLocation location);

        void removeGeoFriends(String key);

        void showLeftComment(String message);

        void showRightComment(String message);

        void noComment();

        void showUserExp(int userExp, int nextExp, int barLength, int barLengthMax);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openGoogleMaps(Location location);

        void queryfriendlocation(Location location);

        void setUserStatus(boolean isChecked);

        void setUserPhoto();

        void setUserExp(int userExp);

        void sendMessage(String cheermessage);

        void noMessage();

        void getFriendMessage();
    }
}
