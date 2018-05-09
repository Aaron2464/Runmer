package com.example.aaron.runmer.FriendsList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aaron.runmer.R;

public class FriendsListAdapter extends RecyclerView.Adapter {

    private FriendsListContract.Presenter mPresenter;
    public FriendsListAdapter(FriendsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class FriendsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button mBtnLocation;
        private Button mBtnAddFriend;
        private Button mBtnDenyFriend;
        private ImageView mImageFriendAvatar;
        private TextView mTxtFriendName;
        private TextView mTxtFriendLevel;

        public FriendsListViewHolder(View itemView) {
            super(itemView);
            mBtnLocation = itemView.findViewById(R.id.btn_location);
            mBtnAddFriend = itemView.findViewById(R.id.btn_add_friend);
            mBtnDenyFriend = itemView.findViewById(R.id.btn_deny_friend);
            mImageFriendAvatar = itemView.findViewById(R.id.image_friend_avatar);
            mTxtFriendName = itemView.findViewById(R.id.txt_friend_name);
            mTxtFriendLevel = itemView.findViewById(R.id.txt_friend_level);

            mBtnLocation.setOnClickListener(this);
            mBtnAddFriend.setOnClickListener(this);
            mBtnDenyFriend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btn_location:
                    break;
                case R.id.btn_add_friend:
                    break;
                case R.id.btn_deny_friend:
                    break;
                default:
                    break;
            }
        }

        public Button getBtnAddFriend(){
            return mBtnAddFriend;
        }
        public Button getBtnDenyFriend(){
            return mBtnDenyFriend;
        }
        public Button getBtnLocation(){
            return mBtnLocation;
        }
    }
}
