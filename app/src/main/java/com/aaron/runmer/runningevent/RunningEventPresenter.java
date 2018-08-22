package com.aaron.runmer.runningevent;

import android.util.Log;

import com.aaron.runmer.api.RunmerParser;
import com.aaron.runmer.api.callback.SetEventPeopleJoinCallback;
import com.aaron.runmer.objects.EventData;
import com.aaron.runmer.util.Constants;
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
    public void clickRunningEventFabBtn() {
        mRunningEventView.showRunningEventFabDialog();
    }

    @Override
    public void setEventDataToFirebase(EventData eventData) {
        RunmerParser.parseFirebaseRunningEvent(eventData);
    }

    @Override
    public void setEventPeopleParticipate(final int position, String eventId) {
        RunmerParser.parseFirebaseJoinRunningEvent(eventId, new SetEventPeopleJoinCallback() {
            @Override
            public void onCompleted(EventData eventData) {
                mRunningEventView.addPeopleRunningEventList(position, eventData);
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    @Override
    public void quertAllRunningEvent() {
        final FirebaseAuth mCurrentUserUid = FirebaseAuth.getInstance();
        final DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();

        dataBaseRef.child(Constants.EVENT_FIREBASE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String key) {
                Log.d(Constants.TAG, "key" + key);
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
