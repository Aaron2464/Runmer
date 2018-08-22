package com.aaron.runmer.friendslist;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;
import com.aaron.runmer.objects.FriendData;
import com.aaron.runmer.objects.UserData;

public interface FriendsListContract {

    interface View extends BaseView<Presenter> {

        void showFriendInformation(UserData foundUser);

        void showFriendList(FriendData friendData);

        void removeFriendList(int position);

        void showFriendListFabDialog();

        void showNonFriend();

        void showInviteSuccess();
    }

    interface Presenter extends BasePresenter {

        void clickFriendListFabbtn();

        void queryAllFriendData();

        void searchFriend(String friendEmail);

        void addFriend(String s);

        void denyFriend(String removeFriendUid, int adapterPosition);

        void sendFriendInvitation(String inviteFriendEmail);

    }
}
