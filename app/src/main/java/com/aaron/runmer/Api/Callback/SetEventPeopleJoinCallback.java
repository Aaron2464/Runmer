package com.aaron.runmer.Api.Callback;

import com.aaron.runmer.Objects.EventData;

public interface SetEventPeopleJoinCallback {

    public void onCompleted(EventData value);

    public void onError(String errorMessage);
}
