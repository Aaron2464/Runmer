package com.example.aaron.runmer.Api;

import android.util.Log;

import com.example.aaron.runmer.Api.Callback.InvitehFireBaseUserDataCallback;
import com.example.aaron.runmer.Api.Callback.SearchFireBaseFriendDataCallback;
import com.example.aaron.runmer.Api.Callback.firebaseShowAddedFriendDataCallback;
import com.example.aaron.runmer.Objects.EventData;
import com.example.aaron.runmer.Objects.FriendData;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RunmerParser {

    public final static FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
    public final static DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
    public final static String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();

    public static void parseFireBaseFriendData(final String searchData, final SearchFireBaseFriendDataCallback parseFireBaseFriendCallback) {

//        DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(searchData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Boolean searchResultBean = true;
                        UserData mUserData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            mUserData = childSnapshot.getValue(UserData.class);
                        }
                        if (mUserData.getUserEmail() != null) {
                            if (mUserData.getUserEmail().equals(searchData)) {
                                Log.d(Constants.TAG, mUserData.getUserEmail().toString());
                                Log.d(Constants.TAG, mUserData.getUserName().toString());
                                Log.d(Constants.TAG, mUserData.getUserPhoto().toString());
                                parseFireBaseFriendCallback.onCompleted(searchResultBean, mUserData);
                            } else {
                                searchResultBean = false;
                                parseFireBaseFriendCallback.onCompleted(searchResultBean, new UserData());
                            }
                        } else {
                            parseFireBaseFriendCallback.onError("There is no user! ");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public static void parseFireBaseInviteFriend(final String inviteData, final InvitehFireBaseUserDataCallback parseFireBaseInviteFriendCallback) {

//        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
//        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(inviteData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        UserData mUserData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            mUserData = childSnapshot.getValue(UserData.class);
                        }
                        String inviteFriendUid = String.valueOf(mUserData.getUserUid());
                        Log.d(Constants.TAG, "inviteFriendUid : " + inviteFriendUid);
                        String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
                        Log.d(Constants.TAG, "currentUserUid : " + currentUserUid);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).child("FriendRequest").setValue("invited");
//                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).setValue(UserData.class);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(inviteFriendUid).child("FriendRequest").setValue("waiting");
                        parseFireBaseInviteFriendCallback.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public static void parseFireBaseAddFriend(String friendEmail) {
//        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
//        final String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
//        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(friendEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        UserData mUserData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            mUserData = childSnapshot.getValue(UserData.class);
                        }
                        final String inviteFriendUid = String.valueOf(mUserData.getUserUid());
                        Log.d(Constants.TAG, "inviteFriendUid : " + inviteFriendUid);
                        Log.d(Constants.TAG, "currentUserUid : " + currentUserUid);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(inviteFriendUid).child("FriendRequest").setValue("true");
                        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_UID).equalTo(currentUserUid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        UserData mUserData = new UserData();
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            mUserData = childSnapshot.getValue(UserData.class);
                                        }
                                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).setValue(mUserData);
                                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).child("FriendRequest").setValue("true");
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public static void parseFirebaseShowAddedFriend(final String friendUid) {

        dataBaseRef.child(Constants.USER_FIREBASE).child(friendUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        FriendData mUserData = snapshot.getValue(FriendData.class);
                        String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(friendUid).setValue(mUserData);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(friendUid).child("FriendRequest").setValue("invited");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public static void parseFirebaseRemoveFriend(String removeFriendUid){
//        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
//        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child(Constants.USER_FIREBASE_FRIENDS).child(removeFriendUid).removeValue();
        dataBaseRef.child(Constants.USER_FIREBASE).child(removeFriendUid).child(Constants.USER_FIREBASE_FRIENDS).child(currentUserUid).removeValue();
    }

    public static void parseFirebaseRunningEvent(EventData mEventData) {
//        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
//        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();

        Log.d(Constants.TAG,"EVENTUID: " + currentUserUid);
        dataBaseRef.child(Constants.EVENT_FIREBASE).child(currentUserUid).push().setValue(mEventData);

    }
}
