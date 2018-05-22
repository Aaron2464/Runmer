package com.example.aaron.runmer.RunningEvent;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;

public interface RunningEventContract {

    interface View extends BaseView<Presenter> {

        void showRunningEventFABDialog();

    }

    interface Presenter extends BasePresenter {

        void clickRunningEventFABbtn();
    }
}
