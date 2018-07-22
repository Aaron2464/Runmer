package com.aaron.runmer.Map;

import android.location.Location;
import android.util.Log;

import com.aaron.runmer.util.Constants;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPresenter implements MapContract.Presenter {

    private int mNextExp, mBarLength, mBarLengthMax;

    private final MapContract.View mMapsView;
    private String mUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mFriendRef = FirebaseDatabase.getInstance().getReference(Constants.USER_FIREBASE).child(mUserUid).child("Friends");
    private DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference(Constants.USER_FIREBASE).child(mUserUid);
    private DatabaseReference mUserLocation = FirebaseDatabase.getInstance().getReference("Location");
    private DatabaseReference mComment = FirebaseDatabase.getInstance().getReference("Comments");
    private GeoFire mGeoFire = new GeoFire(mUserLocation);
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

            GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), 0.03);
            geoQuery.removeAllListeners();

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {
                    Log.d(Constants.TAG, "onKeyEntered");
                    if (!key.equals(mAuth.getCurrentUser().getUid())) {
                        mFriendRef.child(key).child(Constants.USER_FIREBASE_PHOTO).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            String friendAvatar = dataSnapshot.getValue().toString();
                                            Log.d(Constants.TAG, "PKEY: " + key);
                                            Log.d(Constants.TAG, "PUri: " + friendAvatar.toString());
                                            mMapsView.showGeoFriends(key, location, friendAvatar);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
//------------------------------------以下註解是白癡行為...竟然花了我快半年研究，特別在此留下----------------------------------------------//
////                    FirebaseDatabase.getInstance().getReference("Users")
//                    Log.d(Constants.TAG, "Key: " + key);
//                    mFriendRef.addListenerForSingleValueEvent(new ValueEventListener() { //addValueEventListener
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot postsnot : dataSnapshot.getChildren()) {
//                                String Uid = postsnot.getKey().toString();
//                                Log.d(Constants.TAG, "QueryFriendUid: " + postsnot.getKey());
//                                if (!key.equals(mAuth.getCurrentUser().getUid()) && key.equals(Uid)) {           //判斷是不是自己還有拿key才要先geofire setlocation確定有enter，*重點!!
//                                    LatLng userlocation = new LatLng(Double.parseDouble(postsnot.child("lat").getValue().toString())
//                                            , Double.parseDouble(postsnot.child("lng").getValue().toString()));
//                                    Log.d(Constants.TAG,"FriendLocation: " + userlocation);
//                                    mMapsView.showGeoFriends(userlocation);
//
//                                } else {
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//------------------------------------https://ppt.cc/fVwvWx-------關鍵網頁---------------------------------------------------------------------------------//
//------------------------------------以上註解是白癡行為...竟然花了我快半年研究，特別在此留下----------------------------------------------//
                }

                @Override
                public void onKeyExited(String key) {
                    Log.d(Constants.TAG, "onKeyExited");
                    mMapsView.removeGeoFriends(key);
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    mMapsView.moveGeoFriends(key, location);
                }

                @Override
                public void onGeoQueryReady() {
                    mComment.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Log.d(Constants.TAG, "GeoQuery: " + String.valueOf(dataSnapshot));
                            String dskey = dataSnapshot.getKey();
                            Log.d(Constants.TAG, "GeoQuery: " + dskey);
                            Log.d(Constants.TAG, "GeoQuery: " + dataSnapshot.child("UserUid").getValue());
                            if (!dataSnapshot.child("UserUid").getValue().equals(mAuth.getCurrentUser().getUid())) {
                                String message = String.valueOf(dataSnapshot.child("CommentId").getValue());
                                Log.d(Constants.TAG, "GeoQuery: " + message);
                                mMapsView.showRightComment(message);
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void setUserStatus(boolean isChecked) {
        mUserRef.child("userStatus").setValue(isChecked);
    }

    @Override
    public void setUserPhoto() {
        mUserRef.child("userPhoto").addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void setUserExp(int userExp) {
        if (userExp < 10000) {
            mNextExp = 10000;
            mBarLength = userExp;
            mBarLengthMax = mNextExp;
            mMapsView.showUserExp(userExp, mNextExp, mBarLength, mBarLengthMax);
        } else if (userExp >= 10000 && userExp < 25000) {
            mNextExp = 25000;
            mBarLength = userExp - 10000;
            mBarLengthMax = mNextExp - 10000;
            mMapsView.showUserExp(userExp, mNextExp, mBarLength, mBarLengthMax);
        } else if (userExp >= 25000 && userExp < 45000) {
            mNextExp = 45000;
            mBarLength = userExp - 25000;
            mBarLengthMax = mNextExp - 25000;
            mMapsView.showUserExp(userExp, mNextExp, mBarLength, mBarLengthMax);
        } else {
            mNextExp = 80000;
            mBarLength = userExp - 45000;
            mBarLengthMax = mNextExp - 45000;
            mMapsView.showUserExp(userExp, mNextExp, mBarLength, mBarLengthMax);
        }
    }

    @Override
    public void sendMessage(String cheermessage) {
        String getkey = mComment.push().getKey();
        mComment.child(getkey).child("CommentId").setValue(cheermessage);
        mComment.child(getkey).child("UserUid").setValue(mUserUid);
        mMapsView.showLeftComment(cheermessage);
    }

    @Override
    public void noMessage() {
        mMapsView.noComment();
    }

    @Override
    public void getFriendMessage() {
    }

    @Override
    public void start() {

    }
}
