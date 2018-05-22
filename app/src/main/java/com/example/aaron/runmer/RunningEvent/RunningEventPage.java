package com.example.aaron.runmer.RunningEvent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaron.runmer.Objects.EventData;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.Constants;
import com.example.aaron.runmer.util.LinearItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPage extends Fragment implements RunningEventContract.View {

    RecyclerView mRecyclerView;
    RunningEventAdapter mAdapter;
    FloatingActionButton mFAB;
    TextInputEditText mEditTxtEventTitle;
    TextInputEditText mEditTxtEventPlace;
    TextInputEditText mEditTxtEventNumOfPeople;
    ConstraintLayout mRunningeventLayout;
    private HashMap<String,String> UserMap=new  HashMap<String,String>();
    private ArrayList<EventData> ArrayEventData = new ArrayList<>();
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
        mRunningeventLayout = view.findViewById(R.id.runningeventLayout);
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
        mFAB = view.findViewById(R.id.fab_runningevent);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clickRunningEventFABbtn();
            }
        });
    }

    @Override
    public void showRunningEventFABDialog() {
        LayoutInflater dialodInflater = LayoutInflater.from(getContext());
        View dialogView = dialodInflater.inflate(R.layout.dialog_event_create, null);
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
                        if(TextUtils.isEmpty(mEditTxtEventTitle.getText().toString())
                                || TextUtils.isEmpty(mEditTxtEventPlace.getText().toString())
                                || TextUtils.isEmpty(mEditTxtEventNumOfPeople.getText().toString())){
                            dialog.dismiss();
                            Snackbar.make(mRunningeventLayout,"青春不留白，走過必留下痕跡 ",Snackbar.LENGTH_SHORT).show();
                        }else{
                            String eventTitle = mEditTxtEventTitle.getText().toString();
                            String eventPlace = mEditTxtEventPlace.getText().toString();
                            String eventNumOfPeople = mEditTxtEventNumOfPeople.getText().toString();
                            String userName = getContext().getSharedPreferences(Constants.USER_FIREBASE, Context.MODE_PRIVATE).getString(Constants.USER_FIREBASE_NAME,"");
                            String userPhoto = getContext().getSharedPreferences(Constants.USER_FIREBASE,Context.MODE_PRIVATE).getString(Constants.USER_FIREBASE_PHOTO,"");

                            EventData mEventData = new EventData();
                            mEventData.setMasterName(userName);
                            mEventData.setMasterPhoto(userPhoto);
                            mEventData.setEventTitle(eventTitle);
                            mEventData.setEventPlace(eventPlace);
                            mEventData.setPeopleParticipate("1");
                            mEventData.setPeopleTotle(eventNumOfPeople);

                            Log.d(Constants.TAG,"EventData: " + userName);

                            mPresenter.setEventDataToFirebase(mEventData);
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
    public void showRunningEventList(EventData mEventData) {
        ArrayEventData.add(mEventData);
        mAdapter = new RunningEventAdapter(getContext(),ArrayEventData);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setPresenter(RunningEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
