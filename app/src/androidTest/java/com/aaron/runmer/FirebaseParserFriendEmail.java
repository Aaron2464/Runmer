package com.aaron.runmer;

import android.support.test.runner.AndroidJUnit4;

import com.aaron.runmer.api.RunmerParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)

public class FirebaseParserFriendEmail {
    @Mock
    private RunmerParser mRunmerParser;

    @Test
    public void firebaseParser() {
        MockitoAnnotations.initMocks(this);

    }
}