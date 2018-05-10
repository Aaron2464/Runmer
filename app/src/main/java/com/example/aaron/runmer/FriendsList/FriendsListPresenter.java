package com.example.aaron.runmer.FriendsList;

import android.util.Log;

import com.example.aaron.runmer.Api.Callback.AddFriendFireBaseCallback;
import com.example.aaron.runmer.Api.Callback.InvitehFireBaseUserDataCallback;
import com.example.aaron.runmer.Api.Callback.SearchFireBaseUserDataCallback;
import com.example.aaron.runmer.Api.RunmerParser;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.util.Constants;

import java.util.ArrayList;

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
    public void loadFriendData() {

    }

    @Override
    public void searchFriend(String friendEmail) {
        // keep checking users on fireBase and report it if there's one that is matched
        RunmerParser.parseFireBaseUserData(friendEmail, new SearchFireBaseUserDataCallback() {
            @Override
            public void onCompleted(Boolean searchResult, UserData foundUser) {
                if (searchResult == true) {
                    mFriendsListView.showFriendInformation(foundUser);
                } else {
                    mFriendsListView.showNonFriend();
                    //TODO 沒有東西的時候會報錯
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(Constants.TAG, errorMessage);
            }
        });
    }

    @Override
    public void addFriend(String addFriendEmail) {

        RunmerParser.parseFireBaseAddFriend(addFriendEmail, new AddFriendFireBaseCallback() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void denyFriend() {

    }

    @Override
    public void sendFriendInvitation(String inviteFriendEmail) {
        Log.d(Constants.TAG, "inviteFriendEmail : " + inviteFriendEmail);
        RunmerParser.parseFireBaseInviteFriend(inviteFriendEmail, new InvitehFireBaseUserDataCallback() {
            @Override
            public void onCompleted(UserData inviteUser) {
                String inviteFriendUid = inviteUser.getUserUid();
                mFriendsListView.showFriendList(inviteUser);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d(Constants.TAG, errorMessage);
            }
        });
    }

    @Override
    public void start() {

    }
}
