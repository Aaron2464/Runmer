package com.example.aaron.runmer.Map;

import android.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPage  extends Fragment implements MapContract.View{

    private MapContract.Presenter mPresenter;

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}