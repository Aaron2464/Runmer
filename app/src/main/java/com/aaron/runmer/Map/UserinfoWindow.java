package com.aaron.runmer.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.aaron.runmer.R;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserinfoWindow implements GoogleMap.InfoWindowAdapter {
    View myView;
    Context mContext;
    private HashMap<String, String> mUriMap;

//    String mfriendAvatar;

    public UserinfoWindow(Context context, Map<String, String> mUriMap) {
//        mfriendAvatar = friendAvatar;
        this.mUriMap = (HashMap<String, String>) mUriMap;
        mContext = context;
        myView = LayoutInflater.from(context).inflate(R.layout.mappage_friend_image, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
//                TextView txtPickupTitle = (TextView)myView.findViewById(R.id.txtInfo);
        ImageView imageFriendAvatar = myView.findViewById(R.id.image_geofriend_avatar);
        Log.d(Constants.TAG, "UMarkerId: " + marker.getId());

        if (mUriMap.containsKey(marker.getId())) {
            imageFriendAvatar.setVisibility(View.VISIBLE);
            Picasso.get().load(mUriMap.get(marker.getId())).transform(new CircleTransform(mContext)).placeholder(R.drawable.user_image).into(imageFriendAvatar);
            Log.d(Constants.TAG, "UUri: " + mUriMap.get(marker.getId()).toString());
        } else {
            imageFriendAvatar.setVisibility(View.GONE);
        }
        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
