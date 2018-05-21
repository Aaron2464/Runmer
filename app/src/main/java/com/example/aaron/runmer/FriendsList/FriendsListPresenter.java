package com.example.aaron.runmer.FriendsList;

import android.util.Log;

import com.example.aaron.runmer.Api.Callback.InvitehFireBaseUserDataCallback;
import com.example.aaron.runmer.Api.Callback.SearchFireBaseFriendDataCallback;
import com.example.aaron.runmer.Api.RunmerParser;
import com.example.aaron.runmer.Objects.FriendData;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsListPresenter implements FriendsListContract.Presenter {

    private FriendsListContract.View mFriendsListView;
    private boolean mLoading = false;

    public FriendsListPresenter(FriendsListContract.View FriendsListview) {
        mFriendsListView = checkNotNull(FriendsListview);
        mFriendsListView.setPresenter(this);
    }

    @Override
    public void clickFABbtn() {
        mFriendsListView.showFABDialog();
    }

    @Override
    public void queryAllFriendData() {
        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
        final String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        dataBaseRef.child(Constants.USER_FIREBASE)
                .child(currentUserUid)
                .child(Constants.USER_FIREBASE_FRIENDS)
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("FriendRequest").getValue().equals("invited")) {
                            Log.d(Constants.TAG, "FriendRequest: " + dataSnapshot.child("FriendRequest").getValue());
                            Log.d(Constants.TAG, "Uid: " + dataSnapshot.getKey());
                            RunmerParser.parseFirebaseShowAddedFriend(dataSnapshot.getKey());
                        } else if (dataSnapshot.child("FriendRequest").getValue().equals("waiting")) {
                            Log.d(Constants.TAG, "FriendRequest: " + dataSnapshot.child("FriendRequest").getValue());
                        } else {
                            mFriendsListView.showFriendList(dataSnapshot.getValue(FriendData.class));
                            Log.d(Constants.TAG, "onChildAdded.FriendRequest: " + dataSnapshot.child("userName").getValue());
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String uid) {
                        FriendData mfriendData;
                        mfriendData = dataSnapshot.getValue(FriendData.class);
                        Log.d(Constants.TAG, "onChildChanged.FriendRequest: " + mfriendData.getFriendRequest());
                        Log.d(Constants.TAG, "onChildChanged. s: " + uid);

                        if (mfriendData.getFriendRequest() == null) {
                        } else {
                            if (mfriendData.getFriendRequest() == "true"){
                            }
                            else {
                            mFriendsListView.showFriendList(dataSnapshot.getValue(FriendData.class));
                            }
                        }
                        Log.d(Constants.TAG, "onChildChanged.FriendRequest: " + dataSnapshot.child("userName").getValue());
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
    public void searchFriend(String friendEmail) {
        // keep checking users on fireBase and report it if there's one that is matched
        RunmerParser.parseFireBaseFriendData(friendEmail, new SearchFireBaseFriendDataCallback() {
            @Override
            public void onCompleted(Boolean searchResult, UserData foundUser) {
                if (searchResult == true) {
                    mFriendsListView.showFriendInformation(foundUser);
                } else {
                    mFriendsListView.showNonFriend();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(Constants.TAG, errorMessage);
                mFriendsListView.showNonFriend();
            }
        });
    }

    @Override
    public void addFriend(String addFriendEmail) {
        RunmerParser.parseFireBaseAddFriend(addFriendEmail);
    }

    @Override
    public void denyFriend(String removeFriendUid, int adapterPosition) {
        mFriendsListView.removeFriendList(adapterPosition);
        RunmerParser.parseFirebaseRemoveFriend(removeFriendUid);
    }

    @Override
    public void sendFriendInvitation(String inviteFriendEmail) {
        Log.d(Constants.TAG, "inviteFriendEmail : " + inviteFriendEmail);
        RunmerParser.parseFireBaseInviteFriend(inviteFriendEmail, new InvitehFireBaseUserDataCallback() {
            @Override
            public void onCompleted() {
                mFriendsListView.showInviteSuccess();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(Constants.TAG, errorMessage);
                mFriendsListView.showNonFriend();
            }
        });
    }

    @Override
    public void start() {

    }
}
