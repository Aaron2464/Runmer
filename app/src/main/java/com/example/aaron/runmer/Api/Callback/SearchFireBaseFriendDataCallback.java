package com.example.aaron.runmer.Api.Callback;


import com.example.aaron.runmer.Objects.UserData;

public interface SearchFireBaseFriendDataCallback {

    public void onCompleted(Boolean bean, UserData foundUser);

    public void onError(String errorMessage);
}
