package com.aaron.runmer.Api.Callback;

public interface CountCreatedJoinedCallback {

    public void onCompleted(int CountCreatedEvents);

    public void onError(String errorMessage);
}
