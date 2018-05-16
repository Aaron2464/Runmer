package com.example.aaron.runmer.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
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
        Log.d(Constants.TAG, "UUri: " + mUriMap.get(marker.getId()).toString());
        Picasso.get().load(mUriMap.get(marker.getId())).transform(new CircleTransform(mContext)).placeholder(R.drawable.user_image).into(imageFriendAvatar);
        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
