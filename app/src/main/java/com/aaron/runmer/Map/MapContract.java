package com.aaron.runmer.Map;

import android.location.Location;

import com.aaron.runmer.Base.BasePresenter;
import com.aaron.runmer.Base.BaseView;
import com.firebase.geofire.GeoLocation;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        void showGoogleMapUi(double lat, double lng);

        void showUserPhoto(String userimage);

        void showGeoFriends(String key, GeoLocation location, String friendAvatar);

        void moveGeoFriends(String key, GeoLocation location);

        void removeGeoFriends(String key);

        void showLeftComment(String Uid, String message);

        void noComment();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void openGoogleMaps(Location mLocation);

        void queryfriendlocation(Location mlocation);

        void setUserStatus(boolean isChecked);

        void setUserPhoto();

        void sendMessage(String cheermessage);

        void noMessage();
    }
}
