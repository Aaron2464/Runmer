package com.example.aaron.runmer.Map;

import android.location.Location;
import android.util.Log;

import com.example.aaron.runmer.util.Constants;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPresenter implements MapContract.Presenter {
    private final MapContract.View mMapsView;
    String mUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mFriendRef = FirebaseDatabase.getInstance().getReference("Users").child(mUserUid).child("Friends");
    DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference("Users").child(mUserUid);
    DatabaseReference mUserLocation = FirebaseDatabase.getInstance().getReference("Location");
    GeoFire mGeoFire = new GeoFire(mUserLocation);
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MapPresenter(MapContract.View mapsView) {
        mMapsView = checkNotNull(mapsView, "mapsView connot be null!");
        mMapsView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void openGoogleMaps(final Location mLocation) {
        if (mLocation != null) {
            final double latitude = mLocation.getLatitude();
            final double longitude = mLocation.getLongitude();

            mUserRef.child("lat").setValue(latitude);
            mUserRef.child("lng").setValue(longitude);
            mGeoFire.setLocation(mUserUid, new GeoLocation(latitude, longitude),
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
    public void queryfriendlocation(Location mLocation) {
        if (mLocation != null) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();

            GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), 0.5);
            geoQuery.removeAllListeners();

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {
//                    FirebaseDatabase.getInstance().getReference("Users")        //TODO 抓資料的key (之後抓friend location)
                    Log.d(Constants.TAG, "Key: " + key);
                    mFriendRef.addListenerForSingleValueEvent(new ValueEventListener() { //addValueEventListener
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postsnot : dataSnapshot.getChildren()) {
                                String Uid = postsnot.getKey().toString();
                                Log.d(Constants.TAG, "QueryFriendUid: " + postsnot.getKey());
                                if (!key.equals(mAuth.getCurrentUser().getUid().toString()) && key.equals(Uid)) {           //判斷是不是自己還有拿key才要先geofire setlocation確定有enter，*重點!!
                                    LatLng userlocation = new LatLng(Double.parseDouble(postsnot.child("lat").getValue().toString())
                                            , Double.parseDouble(postsnot.child("lng").getValue().toString()));
                                    Log.d(Constants.TAG,"FriendLocation: " + userlocation);
                                    mMapsView.showGeoFriends(userlocation);

                                } else {
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void setUserStatus(boolean isChecked) {
        mUserRef.child("UserStatus").setValue(isChecked);
    }

    @Override
    public void setUserPhoto() {
        mUserRef.child("UserPhoto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMapsView.showUserPhoto((String) dataSnapshot.getValue());
                Log.d(Constants.TAG, "UserPhotoo: " + (String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void start() {

    }
}
