package com.example.aaron.runmer.RunningEvent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aaron.runmer.Objects.EventData;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RunningEventAdapter extends RecyclerView.Adapter<RunningEventAdapter.ViewHolder> {

    Context mContext;
    ArrayList<EventData> mEventData;
    public RunningEventAdapter(Context context, ArrayList<EventData> mEventData) {
        mContext = context;
        this.mEventData = mEventData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RunningEventAdapter.ViewHolder holder, int position) {

        holder.mTxtEventTitle.setText(mEventData.get(position).getEventTitle());
        holder.mTxtEventPlace.setText(mEventData.get(position).getEventPlace());
        holder.mTxtCurrentPeopleNum.setText(mEventData.get(position).getPeopleParticipate());
        holder.mTxtPeopleSum.setText(mEventData.get(position).getPeopleTotle());
        Picasso.get().load(mEventData.get(position).getMasterPhoto()).placeholder(R.drawable.running).transform(new CircleTransform(mContext)).into(holder.mImageEventFriendAvatar);

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
                    Log.d(Constants.TAG, "Join~~~~~:" + getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }
}
