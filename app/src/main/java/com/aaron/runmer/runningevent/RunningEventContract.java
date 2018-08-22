package com.aaron.runmer.runningevent;

import com.aaron.runmer.base.BasePresenter;
import com.aaron.runmer.base.BaseView;
import com.aaron.runmer.objects.EventData;

public interface RunningEventContract {

    interface View extends BaseView<Presenter> {

        void showRunningEventFabDialog();

        void showRunningEventList(EventData eventData);

        void addPeopleRunningEventList(int position, EventData eventData);
    }

    interface Presenter extends BasePresenter {

        void clickRunningEventFabBtn();

        void quertAllRunningEvent();

        void setEventDataToFirebase(EventData eventData);

        void setEventPeopleParticipate(int position, String eventId);
    }
}
