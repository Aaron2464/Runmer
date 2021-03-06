package com.aaron.runmer.friendslist;

import android.util.Log;

import com.aaron.runmer.api.RunmerParser;
import com.aaron.runmer.api.callback.InvitehFireBaseUserDataCallback;
import com.aaron.runmer.api.callback.SearchFireBaseFriendDataCallback;
import com.aaron.runmer.objects.FriendData;
import com.aaron.runmer.objects.UserData;
import com.aaron.runmer.util.Constants;
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

    public FriendsListPresenter(FriendsListContract.View friendsListview) {
        mFriendsListView = checkNotNull(friendsListview);
        mFriendsListView.setPresenter(this);
    }

    @Override
    public void clickFriendListFabbtn() {
        mFriendsListView.showFriendListFabDialog();
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
                        if ("invited".equals(dataSnapshot.child("friendRequest").getValue())) {
                            Log.d(Constants.TAG, "FriendRequest: " + dataSnapshot.child("friendRequest").getValue());
                            Log.d(Constants.TAG, "Uid: " + dataSnapshot.getKey());
                            RunmerParser.parseFirebaseShowAddedFriend(dataSnapshot.getKey());
                        } else if ("waiting".equals(dataSnapshot.child("friendRequest").getValue())) {
                            Log.d(Constants.TAG, "FriendRequest: " + dataSnapshot.child("friendRequest").getValue());
                        } else if ("true".equals(dataSnapshot.child("friendRequest").getValue())) {
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

                        if (!"true".equals(mfriendData.getFriendRequest())) {
                            mFriendsListView.showFriendList(dataSnapshot.getValue(FriendData.class));
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
        RunmerParser.parseFirebaseFriendData(friendEmail, new SearchFireBaseFriendDataCallback() {
            @Override
            public void onCompleted(boolean searchResult, UserData foundUser) {
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
        RunmerParser.parseFirebaseAddFriend(addFriendEmail);
    }

    @Override
    public void denyFriend(String removeFriendUid, int adapterPosition) {
        mFriendsListView.removeFriendList(adapterPosition);
        RunmerParser.parseFirebaseRemoveFriend(removeFriendUid);
    }

    @Override
    public void sendFriendInvitation(String inviteFriendEmail) {
        Log.d(Constants.TAG, "inviteFriendEmail : " + inviteFriendEmail);
        RunmerParser.parseFirebaseInviteFriend(inviteFriendEmail, new InvitehFireBaseUserDataCallback() {
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
