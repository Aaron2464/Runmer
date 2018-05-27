package com.aaron.runmer.Api.Callback;

import com.aaron.runmer.Objects.FriendData;

public interface firebaseShowAddedFriendDataCallback {

    public void onCompleted(FriendData mUserData);

    public void onError(String errorMessage);
}
