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

import com.example.aaron.runmer.Objects.FriendData;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private FriendsListContract.Presenter mPresenter;
    private ArrayList<FriendData> mFriendData;
    private Context mContext;

    public FriendsListAdapter(Context context, ArrayList<FriendData> userData, FriendsListContract.Presenter presenter) {
        mPresenter = presenter;
        mFriendData = userData;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
                //TODO if FriendRequest is waiting, don't show friend list
                //TODO if FriendRequest is invited, show friend list & check/deny btn. And send UserData
                //TODO if FriendRequest is true, show friend list & hide check/deny btn
                //TODO if UserStatus is true ,show location marker
                //TODO if UserStatus is false, hide  location marker
        holder.mTxtFriendName.setText(mFriendData.get(position).getUserName());
        holder.mTxtFriendLevel.setText(mFriendData.get(position).getUserEmail());
        Picasso.get().load(mFriendData.get(position).getUserPhoto()).placeholder(R.drawable.user_image).transform(new CircleTransform(mContext)).into(holder.mImageFriendAvatar);
    }

    @Override
    public int getItemCount() {
        Log.d(Constants.TAG, "mUserData" + mFriendData);
        return mFriendData.size();
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
            //TODO if click denybtn, delet send UserData.
            //TODO if click checkbtn, change FriendRequest and send UserData
            //TODO if click location marker show map fragment
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
