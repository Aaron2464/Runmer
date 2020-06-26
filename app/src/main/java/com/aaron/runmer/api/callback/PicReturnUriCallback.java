package com.aaron.runmer.api.callback;

import android.net.Uri;

public interface PicReturnUriCallback {
    public void onCompleted(Uri picReturnUri);

    public void onError(String errorMessage);
}
