package com.aaron.runmer.Api.Callback;

public interface CountEventsJoinedCallback {

    public void onCompleted(int CountJoinedEvents);

    public void onError(String errorMessage);
}
