package com.aaron.runmer;

import android.support.test.runner.AndroidJUnit4;

import com.aaron.runmer.Api.RunmerParser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class FirebaseParserFriendEmail {
    @Test
    public void firebaseParser() throws Exception{
        RunmerParser.parseFirebaseAddFriend("htim2464@gmail.com");
    }
}