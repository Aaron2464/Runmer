package com.example.aaron.runmer.UserProfile;

import android.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfilePage extends Fragment implements UserProfileContract.View{

    private UserProfileContract.Presenter mPresenter;
    @Override
    public void setPresenter(UserProfileContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
