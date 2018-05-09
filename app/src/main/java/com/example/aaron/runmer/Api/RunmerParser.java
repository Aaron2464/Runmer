package com.example.aaron.runmer.Api;

import android.util.Log;
import android.widget.Toast;

import com.example.aaron.runmer.Api.Callback.SearchFireBaseUserDataCallback;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RunmerParser {
    UserData mUserData;

    public static void parseFireBaseUserData(final String searchData, final SearchFireBaseUserDataCallback parseFireBaseUserNameCallback) {

        DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
        dataBaseRef.child(Constants.USER_FIREBASE).orderByChild(Constants.USER_FIREBASE_EMAIL).equalTo(searchData)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Boolean searchResultBean = true;
                        UserData mUserData = new UserData();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            mUserData = childSnapshot.getValue(UserData.class);
                        }
                        if(mUserData.getUserEmail().equals(searchData)) {
                            Log.d(Constants.TAG, mUserData.getUserEmail().toString());
                            Log.d(Constants.TAG, mUserData.getUserName().toString());
                            Log.d(Constants.TAG, mUserData.getUserPhoto().toString());
                            parseFireBaseUserNameCallback.onCompleted(searchResultBean, mUserData);
                        }
                        else{
                            searchResultBean = false;           //TODO 沒有東西的時候會報錯
                            parseFireBaseUserNameCallback.onCompleted(searchResultBean, new UserData());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}
