package com.aaron.runmer.api;

import android.util.Log;

import com.aaron.runmer.api.callback.CountEventsCreatedCallback;
import com.aaron.runmer.api.callback.CountEventsJoinedCallback;
import com.aaron.runmer.api.callback.InvitehFireBaseUserDataCallback;
import com.aaron.runmer.api.callback.SearchFireBaseFriendDataCallback;
import com.aaron.runmer.api.callback.SetEventPeopleJoinCallback;
import com.aaron.runmer.objects.EventData;
import com.aaron.runmer.objects.FriendData;
import com.aaron.runmer.objects.UserData;
import com.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RunmerParser {

    public static final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
    public static final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
    public static final String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();

    /**
     * search friend data
     *
     * @param searchData
     * @param parseFireBaseFriendCallback
     */
    public static void parseFirebaseFriendData(final String searchData, final SearchFireBaseFriendDataCallback parseFireBaseFriendCallback) {
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(searchData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Boolean searchResultBean = true;
                        UserData userData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            userData = childSnapshot.getValue(UserData.class);
                        }
                        if (userData.getUserEmail() != null) {
                            if (userData.getUserEmail().equals(searchData)) {
                                Log.d(Constants.TAG, userData.getUserEmail().toString());
                                Log.d(Constants.TAG, userData.getUserName().toString());
                                Log.d(Constants.TAG, userData.getUserPhoto().toString());
                                parseFireBaseFriendCallback.onCompleted(searchResultBean, userData);
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

    /**
     * search and send friend request
     *
     * @param inviteData
     * @param parseFireBaseInviteFriendCallback
     */
    public static void parseFirebaseInviteFriend(final String inviteData, final InvitehFireBaseUserDataCallback parseFireBaseInviteFriendCallback) {
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(inviteData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        UserData userData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            userData = childSnapshot.getValue(UserData.class);
                        }
                        String inviteFriendUid = String.valueOf(userData.getUserUid());
                        Log.d(Constants.TAG, "inviteFriendUid : " + inviteFriendUid);
                        String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
                        Log.d(Constants.TAG, "currentUserUid : " + currentUserUid);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).child("friendRequest").setValue("invited");
//                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).setValue(UserData.class);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(inviteFriendUid).child("friendRequest").setValue("waiting");
                        parseFireBaseInviteFriendCallback.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * accept firend invite
     *
     * @param friendEmail
     */
    public static void parseFirebaseAddFriend(String friendEmail) {
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(friendEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        UserData userData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            userData = childSnapshot.getValue(UserData.class);
                        }
                        final String inviteFriendUid = String.valueOf(userData.getUserUid());
                        Log.d(Constants.TAG, "inviteFriendUid : " + inviteFriendUid);
                        Log.d(Constants.TAG, "currentUserUid : " + currentUserUid);
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(inviteFriendUid).child("friendRequest").setValue("true");
                        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_UID).equalTo(currentUserUid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        UserData userData = new UserData();
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            userData = childSnapshot.getValue(UserData.class);
                                        }
                                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).setValue(userData);
                                        dataBaseRef.child(Constants.USER_FIREBASE).child(inviteFriendUid).child("Friends").child(currentUserUid).child("friendRequest").setValue("true");
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
                        FriendData friendData = snapshot.getValue(FriendData.class);
                        friendData.setFriendRequest("invited");
                        String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(friendUid).setValue(friendData);
//                        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child("Friends").child(friendUid).child("friendRequest").setValue("invited");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * deny friend request
     *
     * @param removeFriendUid
     */
    public static void parseFirebaseRemoveFriend(String removeFriendUid) {
        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child(Constants.USER_FIREBASE_FRIENDS).child(removeFriendUid).removeValue();
        dataBaseRef.child(Constants.USER_FIREBASE).child(removeFriendUid).child(Constants.USER_FIREBASE_FRIENDS).child(currentUserUid).removeValue();
    }

    /**
     * Join running event
     *
     * @param eventData
     */
    public static void parseFirebaseRunningEvent(EventData eventData) {
        String getkey = dataBaseRef.child(Constants.EVENT_FIREBASE).push().getKey();
        dataBaseRef.child(Constants.EVENT_FIREBASE).child(getkey).setValue(eventData);
        dataBaseRef.child(Constants.EVENT_FIREBASE).child(getkey).child(Constants.EVENT_FIREBASE_ID).setValue(getkey);
        dataBaseRef.child(Constants.EVENT_FIREBASE).child(getkey).child(Constants.USER_FIREBASE_UID).child(currentUserUid).setValue("Join");
        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child(Constants.EVENT_FIREBASE).child(getkey).setValue("Join");
        Log.d(Constants.TAG, "getKey: " + getkey);
    }

    public static void parseFirebaseJoinRunningEvent(final String eventId, final SetEventPeopleJoinCallback parseFirebaseJoinRunningEventCallback) {
        dataBaseRef.child(Constants.EVENT_FIREBASE).child(eventId).child("peopleParticipate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numOfPeople = Integer.parseInt(dataSnapshot.getValue().toString()) + 1;

                dataBaseRef.child(Constants.EVENT_FIREBASE).child(eventId).child("peopleParticipate").setValue(String.valueOf(numOfPeople));
                dataBaseRef.child(Constants.EVENT_FIREBASE).child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        parseFirebaseJoinRunningEventCallback.onCompleted(dataSnapshot.getValue(EventData.class));
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
        dataBaseRef.child(Constants.EVENT_FIREBASE).child(eventId).child("UserUid").child(currentUserUid).setValue("Join");
        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child(Constants.EVENT_FIREBASE).child(eventId).setValue("Join");
    }

    public static void parseFirebaseEventsJoined(final CountEventsJoinedCallback parseCountEventsJoinedCallback) {
        dataBaseRef.child(Constants.USER_FIREBASE).child(currentUserUid).child(Constants.EVENT_FIREBASE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int countsEventJoined = (int) dataSnapshot.getChildrenCount();
                Log.d(Constants.TAG, "eventCount" + countsEventJoined);
                parseCountEventsJoinedCallback.onCompleted(countsEventJoined);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void parseFirebaseCountEventsCreated(final CountEventsCreatedCallback parseCountEventsCreatedCallback) {
        Query query = dataBaseRef.child(Constants.EVENT_FIREBASE).orderByChild("masterUid").equalTo(currentUserUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int countsEventCreated = (int) dataSnapshot.getChildrenCount();
                Log.d(Constants.TAG, "countsEventCreated: " + countsEventCreated);
                parseCountEventsCreatedCallback.onCompleted(countsEventCreated);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
