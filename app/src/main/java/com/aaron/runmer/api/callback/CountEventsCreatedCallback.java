package com.aaron.runmer.api.callback;

public interface CountEventsCreatedCallback {

    public void onCompleted(int countCreatedEvents);

    public void onError(String errorMessage);
}
