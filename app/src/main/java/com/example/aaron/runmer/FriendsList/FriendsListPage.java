package com.example.aaron.runmer.FriendsList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsListPage extends Fragment implements FriendsListContract.View {

    RecyclerView mRecyclerView;
    FriendsListAdapter mAdapter;
    FloatingActionButton mFAB;

    private FriendsListContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FriendsListAdapter(mPresenter);
        mPresenter = new FriendsListPresenter(this);
    }

    public void showFriendList() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView_friendlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        mFAB = view.findViewById(R.id.fab_friendlist);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clickFABbtn();
            }
        });
    }

    public void showFABDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.dialog_friend_invite, null);
        final EditText mEditTxtFriendEmail = view.findViewById(R.id.edittxt_friend_invite_email);
        Button mBtnSearchFriend = view.findViewById(R.id.btn_search_friend);
        ConstraintLayout mLayout = view.findViewById(R.id.add_friend_detail);

        mBtnSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendEmail = mEditTxtFriendEmail.getText().toString();
                mPresenter.searchFriend(friendEmail);
                Toast.makeText(getContext(), "找朋友找朋友", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(view);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        Log.d(Constants.TAG, "dialog");
//        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialogBuilder.show();
    }

    @Override
    public void setPresenter(FriendsListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
