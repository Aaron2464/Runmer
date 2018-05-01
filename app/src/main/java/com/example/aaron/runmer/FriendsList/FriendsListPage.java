package com.example.aaron.runmer.FriendsList;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsListPage extends Fragment implements FriendsListContract.View{

    private FriendsListContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setPresenter(FriendsListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
