package com.example.aaron.runmer.Api.Callback;

import com.example.aaron.runmer.Objects.FriendData;

public interface firebaseShowAddedFriendDataCallback {

    public void onCompleted(FriendData mUserData);

    public void onError(String errorMessage);
}
