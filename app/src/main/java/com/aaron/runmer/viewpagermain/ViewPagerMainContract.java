package com.aaron.runmer.viewpagermain;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;

public interface ViewPagerMainContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void transToUserProfile();
    }
}
