package com.aaron.runmer.api.callback;

import com.aaron.runmer.objects.EventData;

public interface SetEventPeopleJoinCallback {

    public void onCompleted(EventData value);

    public void onError(String errorMessage);
}
