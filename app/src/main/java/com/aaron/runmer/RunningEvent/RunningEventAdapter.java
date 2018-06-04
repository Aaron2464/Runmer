package com.aaron.runmer.RunningEvent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.runmer.Objects.EventData;
import com.aaron.runmer.R;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RunningEventAdapter extends RecyclerView.Adapter<RunningEventAdapter.ViewHolder> {

    String mUserName;
    Context mContext;
    ArrayList<EventData> mEventData;
    RunningEventContract.Presenter mPresenter;

    public RunningEventAdapter(Context context, ArrayList<EventData> mEventData, RunningEventContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;
        this.mEventData = mEventData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RunningEventAdapter.ViewHolder holder, int position) {
        int newposition = mEventData.size() - position - 1;
        mUserName = mContext.getSharedPreferences(Constants.USER_FIREBASE, Context.MODE_PRIVATE).getString(Constants.USER_FIREBASE_NAME, "");
        holder.mTxtEventTitle.setText(mEventData.get(newposition).getEventTitle());
        holder.mTxtEventPlace.setText(mEventData.get(newposition).getEventPlace());
        holder.mTxtCurrentPeopleNum.setText(mEventData.get(newposition).getPeopleParticipate());
        holder.mTxtPeopleSum.setText(mEventData.get(newposition).getPeopleTotle());
        if (mEventData.get(newposition).getMasterName().equals(mUserName) || mEventData.get(newposition).getUserUid().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.mBtnJoinEvent.setImageResource(R.drawable.joinrunningevent);
            holder.mBtnJoinEvent.setClickable(false);
        }
        Picasso.get().load(mEventData.get(newposition).getMasterPhoto()).placeholder(R.drawable.running).transform(new CircleTransform(mContext)).into(holder.mImageEventFriendAvatar);
    }

    @Override
    public int getItemCount() {
        return mEventData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageEventFriendAvatar;
        private TextView mTxtEventTitle;
        private TextView mTxtEventPlace;
        private TextView mTxtCurrentPeopleNum;
        private TextView mTxtPeopleSum;
        private ImageButton mBtnJoinEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageEventFriendAvatar = itemView.findViewById(R.id.image_event_friendavatar);
            mTxtEventTitle = itemView.findViewById(R.id.txt_event_title);
            mTxtEventPlace = itemView.findViewById(R.id.txt_event_place);
            mTxtCurrentPeopleNum = itemView.findViewById(R.id.txt_event_pcurrentnum);
            mTxtPeopleSum = itemView.findViewById(R.id.txt_event_psum);
            mBtnJoinEvent = itemView.findViewById(R.id.imagebtn_event_joinevent);

            mBtnJoinEvent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imagebtn_event_joinevent:
                    mBtnJoinEvent.setImageResource(R.drawable.joinrunningevent);
                    mBtnJoinEvent.setClickable(false);
                    String mEventId = mEventData.get(mEventData.size() - getAdapterPosition() - 1).getEventId();
                    mPresenter.setEventPeopleParticipate(getAdapterPosition(), mEventId);
                    Log.d(Constants.TAG, "EventId: " + mEventId);
                    Log.d(Constants.TAG, "Join:" + getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }
}
