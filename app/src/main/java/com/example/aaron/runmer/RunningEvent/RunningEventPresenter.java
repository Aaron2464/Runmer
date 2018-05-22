package com.example.aaron.runmer.RunningEvent;

import com.example.aaron.runmer.Api.RunmerParser;
import com.example.aaron.runmer.Objects.EventData;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.UserData.UserDataPage;
import com.example.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPresenter implements RunningEventContract.Presenter {

    private RunningEventContract.View mRunningEventView;

    public RunningEventPresenter(RunningEventContract.View runningEventView) {
        mRunningEventView = checkNotNull(runningEventView);
        mRunningEventView.setPresenter(this);
    }

    @Override
    public void clickRunningEventFABbtn() {
        mRunningEventView.showRunningEventFABDialog();
    }

    @Override
    public void setEventDataToFirebase(EventData mEventData) {

        RunmerParser.parseFirebaseRunningEvent(mEventData);
    }

    @Override
    public void quertAllRunningEvent() {
        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
        final String currentUserUid = mCurrentUserUid.getCurrentUser().getUid();
        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();

        dataBaseRef.child(Constants.EVENT_FIREBASE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mRunningEventView.showRunningEventList(dataSnapshot.getValue(EventData.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void start() {

    }
}
