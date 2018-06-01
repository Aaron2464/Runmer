package com.aaron.runmer.FriendsList;

import com.aaron.runmer.Base.BasePresenter;
import com.aaron.runmer.Base.BaseView;
import com.aaron.runmer.Objects.FriendData;
import com.aaron.runmer.Objects.UserData;

public interface FriendsListContract {

    interface View extends BaseView<Presenter> {

        void showFriendInformation(UserData foundUser);

        void showFriendList(FriendData friendData);

        void removeFriendList(int position);

        void showFriendListFABDialog();

        void showNonFriend();

        void showInviteSuccess();
    }

    interface Presenter extends BasePresenter {

        void clickFriendListFABbtn();

        void queryAllFriendData();

        void searchFriend(String friendEmail);

        void addFriend(String s);

        void denyFriend(String removeFriendUid, int adapterPosition);

        void sendFriendInvitation(String inviteFriendEmail);

    }
}
