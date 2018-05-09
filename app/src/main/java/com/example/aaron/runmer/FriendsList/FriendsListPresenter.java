package com.example.aaron.runmer.FriendsList;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsListPresenter implements FriendsListContract.Presenter {
    private FriendsListContract.View mFriendsListView;

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
        
    }

    @Override
    public void addFriend() {

    }

    @Override
    public void denyFriend() {

    }

    @Override
    public void start() {

    }
}
