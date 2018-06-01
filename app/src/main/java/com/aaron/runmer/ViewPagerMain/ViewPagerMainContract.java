package com.aaron.runmer.ViewPagerMain;

import com.aaron.runmer.Base.BasePresenter;
import com.aaron.runmer.Base.BaseView;

public interface ViewPagerMainContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void transToUserProfile();
    }
}
