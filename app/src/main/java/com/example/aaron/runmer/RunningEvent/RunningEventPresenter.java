package com.example.aaron.runmer.RunningEvent;

import com.example.aaron.runmer.Api.RunmerParser;
import com.example.aaron.runmer.Objects.EventData;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.UserData.UserDataPage;

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
    public void setEventDataToFirebase(EventData mEventData) {

        RunmerParser.parseFirebaseRunningEvent(mEventData);
    }

    @Override
    public void quertAllRunningEvent() {
    }

    @Override
    public void start() {

    }
}
