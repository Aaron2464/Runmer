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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsListPage extends Fragment implements FriendsListContract.View {

    RecyclerView mRecyclerView;
    FriendsListAdapter mAdapter;
    FloatingActionButton mFAB;
    View view;
    ArrayList<UserData> ArrayUserData = new ArrayList<>();

    private FriendsListContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FriendsListAdapter(getContext(), new ArrayList<UserData>(), mPresenter);
        mPresenter = new FriendsListPresenter(this);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        view = layoutInflater.inflate(R.layout.dialog_friend_invite, null);
    }

    public void showFriendInformation(UserData foundUser) {
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//        View view = layoutInflater.inflate(R.layout.dialog_friend_invite, null);          //記憶體位置不同
        ConstraintLayout mLayout = view.findViewById(R.id.add_friend_detail);
        ImageView mImageInviteFriendAvatar = view.findViewById(R.id.image_invite_friendavatar);
        TextView mTxtInviteFriendName = view.findViewById(R.id.txt_invite_friendname);
        TextView mTxtInviteFriendLevel = view.findViewById(R.id.txt_invite_friendlevel);
        mLayout.setVisibility(View.VISIBLE);        //uperCamel & lowerCamel different?

        mTxtInviteFriendName.setText(foundUser.getUserName().toString());
        mTxtInviteFriendLevel.setText(foundUser.getUserEmail().toString());
        Picasso.get().load(foundUser.getUserPhoto()).transform(new CircleTransform(getContext())).placeholder(R.drawable.user_image).into(mImageInviteFriendAvatar);
        Log.d(Constants.TAG, "mTxtInviteFriendName : " + foundUser.getUserName());
        Log.d(Constants.TAG, "mTxtInviteFriendLevel : " + foundUser.getUserEmail());
        Log.d(Constants.TAG, "mImageInviteFriendAvatar : " + foundUser.getUserPhoto());

    }

    public void showFriendList(UserData friendList) {
        ArrayUserData.add(friendList);
        mAdapter = new FriendsListAdapter(getContext(), ArrayUserData, mPresenter);
        Log.d(Constants.TAG, "AarayUserData : " + ArrayUserData.get(0).getUserName());
        Log.d(Constants.TAG, "AarayUserData : " + ArrayUserData.get(0).getUserEmail());
        Log.d(Constants.TAG, "AarayUserData : " + ArrayUserData.get(0).getUserPhoto());
        Log.d(Constants.TAG, "AarayUserData : " + ArrayUserData.size());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        //TODO 很多很多BUG要處理，還有error handle Ex:重覆加好友,add deny btn handle
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_friendlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        mPresenter.searchFriendList();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mFAB = view.findViewById(R.id.fab_friendlist);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clickFABbtn();
            }
        });
    }

    public void showFABDialog() {

        final EditText mEditTxtFriendEmail = view.findViewById(R.id.edittxt_friend_invite_email);
        Button mBtnSearchFriend = view.findViewById(R.id.btn_search_friend);
        final TextView mTxtInviteFriendEmail = view.findViewById(R.id.txt_invite_friendlevel);
        ConstraintLayout mLayout = view.findViewById(R.id.add_friend_detail);

        mLayout.setVisibility(View.GONE);
        mBtnSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendEmail = mEditTxtFriendEmail.getText().toString();
                if (friendEmail != null) {
                    mEditTxtFriendEmail.setText("");
                    mPresenter.searchFriend(friendEmail);
                } else {
                    Toast.makeText(getContext(), "找朋友找朋友要輸入正確EMAIL喔", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(view);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mTxtInviteFriendEmail.getText().toString().equals(null)) {
                        } else {
                            mPresenter.sendFriendInvitation(mTxtInviteFriendEmail.getText().toString());
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
//        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialogBuilder.show();
    }

    @Override
    public void showNonFriend() {
        Toast.makeText(getContext(), "There is no user !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(FriendsListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
