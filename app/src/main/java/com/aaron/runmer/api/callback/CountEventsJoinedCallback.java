package com.aaron.runmer.api.callback;

public interface CountEventsJoinedCallback {

    public void onCompleted(int countJoinedEvents);

    public void onError(String errorMessage);
}
