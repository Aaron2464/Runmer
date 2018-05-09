package com.example.aaron.runmer.FriendsList;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;

public interface FriendsListContract {

    interface View extends BaseView<Presenter> {

        void showFriendList();

        void showFABDialog();

    }

    interface Presenter extends BasePresenter {

        void clickFABbtn();

        void loadFriendData();

        void searchFriend(String friendEmail);

        void addFriend();

        void denyFriend();

//        void openGoogleMap();
    }
}
