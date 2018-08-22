package com.aaron.runmer.api.callback;

import com.aaron.runmer.objects.FriendData;

public interface FirebaseShowAddedFriendDataCallback {

    public void onCompleted(FriendData userData);

    public void onError(String errorMessage);
}
