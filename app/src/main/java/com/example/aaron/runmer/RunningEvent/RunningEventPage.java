package com.example.aaron.runmer.RunningEvent;

import android.app.Fragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPage extends Fragment implements RunningEventContract.View{

    private RunningEventContract.Presenter mPresenter;
    @Override
    public void setPresenter(RunningEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
