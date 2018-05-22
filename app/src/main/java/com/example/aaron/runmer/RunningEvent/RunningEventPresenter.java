package com.example.aaron.runmer.RunningEvent;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPresenter implements RunningEventContract.Presenter {

    private RunningEventContract.View mRunningEventView;

    public RunningEventPresenter(RunningEventContract.View runningEventView) {
        mRunningEventView = checkNotNull(runningEventView);
        mRunningEventView.setPresenter(this);
    }

    @Override
    public void clickRunningEventFABbtn() {
        mRunningEventView.showRunningEventFABDialog();
    }

    @Override
    public void start() {

    }
}
