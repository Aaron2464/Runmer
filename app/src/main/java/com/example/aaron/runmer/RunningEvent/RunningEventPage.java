package com.example.aaron.runmer.RunningEvent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.Constants;
import com.example.aaron.runmer.util.LinearItemDecoration;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPage extends Fragment implements RunningEventContract.View {

    RecyclerView mRecyclerView;
    RunningEventAdapter mAdapter;
    FloatingActionButton mFAB;
    TextInputEditText mEditTxtEventTitle;
    TextInputEditText mEditTxtEventPlace;
    TextInputEditText mEditTxtEventNumOfPeople;

    private RunningEventContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RunningEventAdapter(getContext());
        mPresenter = new RunningEventPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_eventlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);   //如果可以確定每個item的高度是固定的，設置這個選項可以提高性能
        mRecyclerView.addItemDecoration(new LinearItemDecoration(1, Color.parseColor("#e7e7e7")));
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
//                showRunningEventFABDialog();
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

                        dialog.cancel();
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
    public void setPresenter(RunningEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
