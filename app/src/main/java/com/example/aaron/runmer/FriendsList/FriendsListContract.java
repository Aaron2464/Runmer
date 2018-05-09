package com.example.aaron.runmer.FriendsList;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;
import com.example.aaron.runmer.Objects.UserData;

public interface FriendsListContract {

    interface View extends BaseView<Presenter> {

        void showFriendInformation(UserData foundUser);

        void showFABDialog();

        void showNonFriend();
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
