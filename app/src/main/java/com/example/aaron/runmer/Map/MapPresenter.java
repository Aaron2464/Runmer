package com.example.aaron.runmer.Map;

import android.location.Location;
import android.util.Log;

import com.example.aaron.runmer.util.Constants;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPresenter implements MapContract.Presenter {
    private final MapContract.View mMapsView;
    String mUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mFriendRef = FirebaseDatabase.getInstance().getReference("Users").child(mUserUid).child("Friends");
    DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference("Users").child(mUserUid);

    public MapPresenter(MapContract.View mapsView) {
        mMapsView = checkNotNull(mapsView, "mapsView connot be null!");
        mMapsView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void openGoogleMaps(Location mLocation) {
        if (mLocation != null) {
            final double latitude = mLocation.getLatitude();
            final double longitude = mLocation.getLongitude();

            GeoFire geoFire = new GeoFire(mFriendRef);
            mUserRef.child("lat").setValue(latitude);
            mUserRef.child("lng").setValue(longitude);
            geoFire.setLocation(mUserUid, new GeoLocation(latitude, longitude),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                            mMapsView.showGoogleMapUi(latitude, longitude);

                        }
                    });
            Log.d(Constants.TAG, "Your Location was changed: " + latitude + longitude);
        } else {
            Log.d(Constants.TAG, "Can not get your location");
        }
    }

    @Override
    public void setUserStatus(boolean isChecked) {
        mUserRef.child("UserStatus").setValue(isChecked);
    }

    @Override
    public void start() {

    }
}
