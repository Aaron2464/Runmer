package com.aaron.runmer.friendsprofile;

import android.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsProfilePage extends Fragment implements FriendsProfileContract.View {
    private FriendsProfileContract.Presenter mPresenter;

    @Override
    public void setPresenter(FriendsProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
