package com.aaron.runmer.api.callback;

import com.aaron.runmer.objects.UserData;

public interface SearchFireBaseFriendDataCallback {

    public void onCompleted(Boolean bean, UserData foundUser);

    public void onError(String errorMessage);
}
