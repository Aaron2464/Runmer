package com.aaron.runmer.runningevent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aaron.runmer.R;
import com.aaron.runmer.objects.EventData;
import com.aaron.runmer.util.Constants;
import com.aaron.runmer.util.LinearItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPage extends Fragment implements RunningEventContract.View {

    private RecyclerView mRecyclerView;
    private RunningEventAdapter mAdapter;
    private FloatingActionButton mFab;
    private TextInputEditText mEditTxtEventTitle;
    private TextInputEditText mEditTxtEventPlace;
    private TextInputEditText mEditTxtEventNumOfPeople;
    private ConstraintLayout mRunningEventLayout;
    private HashMap<String, String> mUserMap = new HashMap<String, String>();
    private ArrayList<EventData> mArrayEventData = new ArrayList<>();
    private RunningEventContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RunningEventPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_eventlist);
        mRunningEventLayout = view.findViewById(R.id.runningeventLayout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);   //如果可以確定每個item的高度是固定的，設置這個選項可以提高性能
        mRecyclerView.addItemDecoration(new LinearItemDecoration(1, Color.parseColor("#e7e7e7")));
        mPresenter.quertAllRunningEvent();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFab = view.findViewById(R.id.fab_runningevent);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clickRunningEventFabBtn();
            }
        });
    }

    @Override
    public void showRunningEventFabDialog() {
        LayoutInflater dialogInflater = LayoutInflater.from(getContext());
        View dialogView = dialogInflater.inflate(R.layout.dialog_event_create, null);
        mEditTxtEventTitle = dialogView.findViewById(R.id.edittxt_event_title);
        mEditTxtEventPlace = dialogView.findViewById(R.id.edittxt_event_place);
        mEditTxtEventNumOfPeople = dialogView.findViewById(R.id.edittxt_event_numofpaople);

        mEditTxtEventTitle.setError(null);
        mEditTxtEventPlace.setError(null);
        mEditTxtEventNumOfPeople.setError(null);

        if (TextUtils.isEmpty(mEditTxtEventTitle.getText())) {
            mEditTxtEventTitle.setError("Please Enter The Title! ");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (TextUtils.isEmpty(mEditTxtEventTitle.getText().toString())
                                || TextUtils.isEmpty(mEditTxtEventPlace.getText().toString())
                                || TextUtils.isEmpty(mEditTxtEventNumOfPeople.getText().toString())) {
                            dialog.dismiss();
                            Snackbar.make(mRunningEventLayout, "青春不留白，走過必留下痕跡 ", Snackbar.LENGTH_SHORT).show();
                        } else {
                            String eventTitle = mEditTxtEventTitle.getText().toString();
                            String eventPlace = mEditTxtEventPlace.getText().toString();
                            String eventNumOfPeople = mEditTxtEventNumOfPeople.getText().toString();
                            String userUid = FirebaseAuth.getInstance().getUid();
                            String userName = getContext().getSharedPreferences(Constants.USER_FIREBASE, Context.MODE_PRIVATE).getString(Constants.USER_FIREBASE_NAME, "");
                            String userPhoto = getContext().getSharedPreferences(Constants.USER_FIREBASE, Context.MODE_PRIVATE).getString(Constants.USER_FIREBASE_PHOTO, "");

                            if (!eventNumOfPeople.equals("0")) {
                                EventData eventData = new EventData();
                                eventData.setMasterName(userName);
                                eventData.setMasterUid(userUid);
                                eventData.setMasterPhoto(userPhoto);
                                eventData.setEventTitle(eventTitle);
                                eventData.setEventPlace(eventPlace);
                                eventData.setPeopleParticipate("1");
                                eventData.setPeopleTotle(eventNumOfPeople);
                                mPresenter.setEventDataToFirebase(eventData);
                            } else {
                                Snackbar.make(mRunningEventLayout, "難道...你不是人?", Snackbar.LENGTH_SHORT).show();
                            }
                            Log.d(Constants.TAG, "EventData: " + userName);
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        Log.d(Constants.TAG, "dialog");
        alertDialogBuilder.show();
    }

    @Override
    public void showRunningEventList(EventData eventData) {
        mArrayEventData.add(eventData);
        mAdapter = new RunningEventAdapter(getContext(), mArrayEventData, mPresenter);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void addPeopleRunningEventList(int position, EventData eventData) {
        mArrayEventData.get(mArrayEventData.size() - position - 1).setPeopleParticipate(eventData.getPeopleParticipate());
        mArrayEventData.get(mArrayEventData.size() - position - 1).setUserUid(eventData.getUserUid());
        mAdapter = new RunningEventAdapter(getContext(), mArrayEventData, mPresenter);
        Log.d(Constants.TAG, "UID: " + mArrayEventData.get(mArrayEventData.size() - position - 1).getEventId());
        mAdapter.notifyItemChanged(mArrayEventData.size() - position - 1, null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void setPresenter(RunningEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
