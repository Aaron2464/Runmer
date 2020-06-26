package com.aaron.runmer.friendslist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aaron.runmer.R;
import com.aaron.runmer.objects.FriendData;
import com.aaron.runmer.objects.UserData;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class FriendsListPage extends Fragment implements FriendsListContract.View {

    RecyclerView mRecyclerView;
    FriendsListAdapter mAdapter;
    FloatingActionButton mFab;
    EditText mEditTxtFriendEmail;
    Button mBtnSearchFriend;
    ImageView mImageInviteFriendAvatar;
    TextView mTxtInviteFriendName;
    TextView mTxtInviteFriendLevel;
    TextView mTxtInviteFriendEmail;
    ConstraintLayout mDialogLayout;
    LinearLayout mLinearLayout;
    ArrayList<FriendData> mArrayUserData = new ArrayList<>();

    private FriendsListContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FriendsListAdapter(getContext(), new ArrayList<FriendData>(), mPresenter);
        mPresenter = new FriendsListPresenter(this);
    }

    public void showFriendInformation(UserData foundUser) {
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//        View view = layoutInflater.inflate(R.layout.dialog_friend_invite, null);          //記憶體位置不同
        mDialogLayout.setVisibility(View.VISIBLE);        //uperCamel & lowerCamel different?

        mTxtInviteFriendName.setText(foundUser.getUserName());
        mTxtInviteFriendEmail.setText(foundUser.getUserEmail());
        Picasso.get().load(foundUser.getUserPhoto()).transform(new CircleTransform(getContext())).placeholder(R.drawable.user_image).into(mImageInviteFriendAvatar);
        Log.d(Constants.TAG, "mTxtInviteFriendName : " + foundUser.getUserName());
        Log.d(Constants.TAG, "mTxtInviteFriendLevel : " + foundUser.getUserEmail());
        Log.d(Constants.TAG, "mImageInviteFriendAvatar : " + foundUser.getUserPhoto());
    }

    public void showFriendList(FriendData friendList) {
        if (friendList == null) {
            mLinearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
            mArrayUserData.add(friendList);
            mAdapter = new FriendsListAdapter(getContext(), mArrayUserData, mPresenter);
            Log.d(Constants.TAG, "AarayUserData : " + mArrayUserData.get(0).getUserName());
            Log.d(Constants.TAG, "AarayUserData : " + mArrayUserData.get(0).getUserEmail());
            Log.d(Constants.TAG, "AarayUserData : " + mArrayUserData.get(0).getUserPhoto());
            Log.d(Constants.TAG, "AarayUserData : " + mArrayUserData.size());
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            //TODO 很多很多BUG要處理，還有error handle Ex:重覆加好友,add deny btn handle
        }
    }

    public void removeFriendList(int position) {
        mArrayUserData.remove(position);
        mAdapter.notifyItemChanged(position);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showInviteSuccess() {
        Toast.makeText(this.getContext(), "Send invitation successful !", Toast.LENGTH_LONG).show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView_friendlist);
        mLinearLayout = view.findViewById(R.id.layout_nofriend);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mPresenter.queryAllFriendData();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFab = view.findViewById(R.id.fab_friendlist);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clickFriendListFabbtn();
            }
        });
    }

    public void showFriendListFabDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if ("".equals(mTxtInviteFriendEmail.getText().toString())) {
                            showNonFriend();
                        } else {
                            mPresenter.sendFriendInvitation(mTxtInviteFriendEmail.getText().toString());
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
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
