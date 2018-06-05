package com.aaron.runmer.Api.Callback;

public interface CountEventsCreatedCallback {

    public void onCompleted(int CountCreatedEvents);

    public void onError(String errorMessage);
}
