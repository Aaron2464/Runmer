package com.example.aaron.runmer.FriendsList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private FriendsListContract.Presenter mPresenter;
    private ArrayList<UserData> mUserData;
    private Context mContext;

    public FriendsListAdapter(Context context, ArrayList<UserData> userData, FriendsListContract.Presenter presenter) {
        mPresenter = presenter;
        mUserData = userData;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTxtFriendName.setText(mUserData.get(position).getUserName());
        holder.mTxtFriendLevel.setText(mUserData.get(position).getUserEmail());
        Picasso.get().load(mUserData.get(position).getUserPhoto()).placeholder(R.drawable.user_image).transform(new CircleTransform(mContext)).into(holder.mImageFriendAvatar);
    }

    @Override
    public int getItemCount() {
        Log.d(Constants.TAG, "mUserData" + mUserData);
        return mUserData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button mBtnLocation;
        private Button mBtnAddFriend;
        private Button mBtnDenyFriend;
        private ImageView mImageFriendAvatar;
        private TextView mTxtFriendName;
        private TextView mTxtFriendLevel;

        public ViewHolder(final View itemView) {
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

            switch (v.getId()) {
                case R.id.btn_location:
                    break;
                case R.id.btn_add_friend:
                    mPresenter.addFriend(mTxtFriendLevel.getText().toString());
                    mBtnAddFriend.setVisibility(View.GONE);
                    mBtnDenyFriend.setVisibility(View.GONE);
                    break;
                case R.id.btn_deny_friend:
                    break;
                default:
                    break;
            }
        }

        public Button getBtnAddFriend() {
            return mBtnAddFriend;
        }

        public Button getBtnDenyFriend() {
            return mBtnDenyFriend;
        }

        public Button getBtnLocation() {
            return mBtnLocation;
        }
    }
}
