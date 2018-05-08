package com.example.aaron.runmer.ViewPagerMain;

import com.example.aaron.runmer.Base.BasePresenter;
import com.example.aaron.runmer.Base.BaseView;

public interface ViewPagerMainContract {
    interface View extends BaseView<ViewPagerMainContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

        void transToUserProfile();
    }
}
